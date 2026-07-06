# UI layer conventions

**[← AGENTS.md](../AGENTS.md)**

MVI shape for a tool's screens: `UiState` / `Action` / `Event`, eager-init observation,
`EventHandler`, and `diViewModel()`. Also covers multi-screen navigation.

> **Sibling docs:** [Architecture for plugins](architecture-for-plugins.md) ·
> [Data & domain](data-and-domain.md) · [Testing your tool](testing-your-tool.md)

---

## MVI shape

Every screen's ViewModel exposes exactly:

- **One `StateFlow<UiState>`** — the entire screen's rendering state, a single immutable
  data class.
- **One `Action` sealed interface** — every user intent the screen can dispatch.
- **One event `Flow`** (backed by a `Channel`) — one-shot side effects (navigation, toasts)
  that must not replay on recomposition/configuration change.
- **One `handleAction(action: Action)` entry point** — the screen's only way to talk back to
  the ViewModel.

```kotlin
internal class MyToolViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _event = Channel<Event>(Channel.BUFFERED)
    val event: Flow<Event> = _event.receiveAsFlow()

    fun handleAction(action: Action) {
        when (action) {
            is Action.Increment -> _uiState.update { it.copy(count = it.count + 1) }
            is Action.Reset -> {
                _uiState.update { it.copy(count = 0) }
                emitEvent(Event.CounterReset)
            }
        }
    }

    private fun emitEvent(event: Event) {
        viewModelScope.launch { _event.send(event) }
    }

    data class UiState(val count: Int = 0)

    sealed interface Action {
        data object Increment : Action
        data object Reset : Action
    }

    sealed interface Event {
        data object CounterReset : Event
    }
}
```

Rules of thumb:

- `UiState` is a `data class` with defaults — the ViewModel should be constructible and
  immediately renderable with no arguments beyond its dependencies.
- `Action` and `Event` are `sealed interface`s with `data object` for parameterless cases and
  `data class` for parameterized ones. This keeps `when` exhaustive at compile time.
- Use `MutableStateFlow` + `.update { }` for state; never mutate `UiState` fields in place.
- Events go through a `Channel` (`Channel.BUFFERED`), not a `SharedFlow` — a `Channel` is
  consumed exactly once, which is what you want for "show this toast" / "navigate here"
  semantics; a `SharedFlow` can replay or drop depending on configuration and is easy to get
  wrong for one-shot effects.

### Eager-init observation

If a ViewModel needs to start observing something (an interactor's `StateFlow`, a ticking
timer) as soon as it's created — not just when the UI first collects — do it in an `init`
block, not lazily on first collection:

```kotlin
internal class HomeViewModel(
    private val interactor: MyToolInteractor,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        observeEnabled()
    }

    private fun observeEnabled() {
        interactor.observeEnabled(viewModelScope)
            .onEach { enabled -> _uiState.update { it.copy(enabled = enabled) } }
            .launchIn(viewModelScope)
    }

    // Action / Event / UiState as above
}
```

If a ViewModel genuinely has nothing to do until the UI subscribes (e.g. the counter example
above), leave `init` empty and say so with a comment — it documents the choice was
intentional, not an oversight.

---

## Composable entry point + `EventHandler`

The Composable function that backs a `ToolComponent.Window` (or other region) resolves its
ViewModel via `diViewModel()`, renders `UiState`, forwards `Action`s, and hands the event
`Flow` to a small `EventHandler`:

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import vision.combat.c4.ds.sdk.ui.component.WindowScaffold
import vision.combat.c4.ds.sdk.ui.component.bar.BackNavTopAppBar
import vision.combat.c4.ds.sdk.ui.util.showToast
import vision.combat.c4.ds.sdk.ui.viewmodel.diViewModel
import com.example.mytool.ui.MyToolViewModel.Action
import com.example.mytool.ui.MyToolViewModel.Event
import com.example.mytool.ui.MyToolViewModel.UiState

@Composable
internal fun MyToolWindow(viewModel: MyToolViewModel = diViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WindowContent(uiState = uiState, onAction = viewModel::handleAction)
    EventHandler(eventFlow = viewModel.event)
}

@Composable
private fun WindowContent(uiState: UiState, onAction: (Action) -> Unit) {
    WindowScaffold(
        topAppBar = { BackNavTopAppBar(title = stringResource(R.string.my_tool_name)) },
        content = { Content(uiState, onAction) },
    )
}

@Composable
private fun EventHandler(eventFlow: Flow<Event>) {
    val context = LocalContext.current
    val resetMessage = stringResource(R.string.my_tool_reset_toast)
    LaunchedEffect(eventFlow) {
        eventFlow.collect { event ->
            when (event) {
                is Event.CounterReset -> context.showToast(resetMessage)
            }
        }
    }
}
```

- `diViewModel()` resolves the ViewModel from the tool's Kodein DI graph — no manual
  `ViewModelProvider.Factory` wiring needed. It requires the ViewModel's constructor
  dependencies to be bound in the tool's DI module (see
  [Architecture for plugins](architecture-for-plugins.md#adding-domain--data-layers)).
- `collectAsStateWithLifecycle()` over `collectAsState()` — it stops collecting when the
  Compose lifecycle isn't started, matching how the rest of the platform handles
  lifecycle-aware state.
- `EventHandler` is a separate private composable so the event-collection `LaunchedEffect`
  has an obvious, single home and doesn't get lost inside a larger content function. Keyed on
  `eventFlow`, which is stable for the life of the ViewModel.
- `WindowScaffold` + `BackNavTopAppBar` are the standard shell for a `ToolComponent.Window`;
  reach for the promoted SDK component catalog for buttons, form fields, dialogs, and other
  building blocks rather than hand-rolling Material widgets — the SDK catalog already
  matches the host's design system.

---

## Multi-screen navigation

For a tool window with more than one screen, add a `Route` sealed type and use `AppNavHost`:

```kotlin
@Keep
@Serializable
internal sealed interface MyToolRoute {
    @Serializable
    data object Home : MyToolRoute

    @Serializable
    data object Settings : MyToolRoute
}

@Composable
internal fun MyToolWindow() {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        startDestination = MyToolRoute.Home,
    ) {
        composable<MyToolRoute.Home> {
            HomeScreen(navigateToSettings = { navController.navigate(MyToolRoute.Settings) })
        }
        composable<MyToolRoute.Settings> {
            SettingsScreen()
        }
    }
}
```

- Each screen gets its own `Screen.kt` + `ViewModel.kt` pair, following the same
  `UiState`/`Action`/`Event` shape as a single-screen tool.
  Every per-screen ViewModel resolves its dependencies from the **same** tool-scoped DI
  graph via `diViewModel()` — there's no need to pass dependencies through navigation
  arguments.
- `@Keep` on the route type protects it from being stripped by minification/R8 since it's
  only referenced via reflection-adjacent `@Serializable` navigation machinery.
- Navigation actions (`navigateToSettings` in the example) are passed down as plain lambdas
  from the top-level window composable, not dispatched as ViewModel `Action`s — navigation is
  a UI-layer concern, not domain state.

---

## What not to do

- Don't collect a `Flow` or observe an interactor directly inside a Composable body — do it
  in the ViewModel and expose `UiState`.
- Don't put one-shot effects (toasts, navigation) into `UiState` — they'll re-fire on every
  recomposition/rotation. Use the `Event` channel.
- Don't reach for `LocalContext` inside a `ViewModel` — it's a UI-layer type. If a ViewModel
  needs isolated storage, resolve a repository through DI instead (see
  [Data & domain](data-and-domain.md)).
