# Architecture for plugins

**[← AGENTS.md](../../AGENTS.md)**

How to shape a single external c4ds tool module: the layer cake, where each piece lives, and
how the pieces are wired together. This doc is deliberately generic — it describes a pattern
you apply inside your own tool's package, not anything about the host's internals.

> **Sibling docs:** [Tool lifecycle & setup](../reference/tool-lifecycle-and-setup.md) ·
> [UI layer conventions](ui-layer-conventions.md) · [Data & domain](data-and-domain.md) ·
> [Testing your tool](../testing/testing-your-tool.md) · [Resource & isolation](../reference/resource-and-isolation.md)

---

## The layer cake

A c4ds tool is a plain Android **Application** module (no `Activity`) that the host loads as
a plugin. Structure it in three layers with **compile-time dependencies pointing inward,
toward Domain** (dependency inversion) — Domain defines repository interfaces and domain
models; Data depends on Domain by implementing those interfaces:

```
UI  ──depends on──>  Domain  <──depends on──  Data
```

| Layer      | Contains                                                                                        | Depends on                                                                                |
|------------|----------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| **UI**     | Composables (`Window`/`Overlay`/etc. content), ViewModels, `UiState`/`Action`/`Event`             | Domain (interactors, repository interfaces)                                              |
| **Domain** | Interactors (use cases), domain models, repository **interfaces**, pure mapping/business logic    | Nothing in UI or Data — Domain depends on neither                                        |
| **Data**   | Repository **implementations**, DAOs/entities, `SharedPreferences` wrappers, mappers to/from domain models | Domain (implements its repository interfaces) + Android framework/SDK storage primitives |

- **Domain depends on neither UI nor Data.** It owns the repository interfaces and domain
  models; it has no compile-time dependency on how those interfaces are implemented.
- **Data depends on Domain**, not the other way around: a repository implementation in the
  Data layer implements an interface declared in Domain (dependency inversion principle,
  DIP). This is what lets you swap a repository's storage mechanism without touching Domain
  or UI.
- **Runtime call flow is separate from compile-time dependency.** At runtime, calls flow
  UI → interactor → repository interface → the concrete Data-layer implementation that DI
  injected in. That call flow does *not* mean Domain depends on Data — the interactor only
  ever references the interface Domain declares; DI is what supplies the Data-layer
  implementation underneath it.

- **ViewModels never touch storage directly.** They call an interactor; the interactor calls
  a repository. This is what keeps ViewModels and interactors unit-testable without an
  Android instrumentation environment — see [Testing your tool](../testing/testing-your-tool.md).
- **Composables never touch a ViewModel's internals.** They read `UiState`, call
  `onAction`/the ViewModel's action handler, and observe one-shot events. See
  [UI layer conventions](ui-layer-conventions.md).
- Not every tool needs all three layers spelled out as separate packages — a one-screen
  counter tool can keep its ViewModel and `UiState` together (see the Single Screen Window
  pattern below). Once a tool needs persistence or an SDK domain interactor, split into
  `data/`, `domain/`, `ui/` packages under the tool's root package, mirroring the layout
  below.

---

## Package layout

Group by **feature**, then by **layer** inside the feature — not by layer at the top level.
This keeps a tool's UI, domain, and data code next to each other and makes each tool easy to
copy as a template for the next one.

```
com.example.mytool/
├── MyTool.kt                  # AbstractTool subclass — wires ToolComponents + DI
├── MyToolDescriptor.kt         # ToolDescriptor subclass — identity + factory
├── data/
│   └── MyToolRepositoryImpl.kt # Implements the repository interface; wraps SharedPreferences / Room / files
├── domain/
│   ├── MyToolInteractor.kt     # Use-case layer between UI and data
│   └── repository/
│       └── MyToolRepository.kt # Repository interface — the contract Domain depends on
├── di/
│   └── MyToolModule.kt         # Kodein DI.Module binding interface → impl, and the interactor
└── ui/
    ├── MyToolWindow.kt         # Composable entry point + diViewModel()
    └── MyToolViewModel.kt      # UiState / Action / Event
```

For a multi-screen tool, add a `ui/<screen>/` package per screen (each with its own
`Screen.kt` + `ViewModel.kt`) plus a `Route.kt` sealed type for navigation. See
[UI layer conventions](ui-layer-conventions.md#multi-screen-navigation).

Keep `MyTool.kt` (`AbstractTool`) and `MyToolDescriptor.kt` (`ToolDescriptor`) as separate
files, as shown above — this is the convention every sample in this repo follows.

---

## Single-screen tool, end to end

The smallest complete tool — no persistence, no domain layer — just an `AbstractTool` and a
window backed by a ViewModel:

```kotlin
internal class MyTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        MyToolWindow()
    }
}

class MyToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.my_tool_name
    override val iconResId: Int = R.drawable.ic_my_tool
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool =
        MyTool(toolContext, this, di, params)
}
```

`requiredComponent { }` declares that this tool must show a window whenever it is active;
the host calls the lambda to build the Compose content the first time it's shown. Declare
only the `ToolComponent`s your tool actually needs (`window`, `mapWindow`, `overlay`,
`status`, `expandableStatus`, `underlay`) — see [Tool lifecycle & setup](../reference/tool-lifecycle-and-setup.md#screen-regions-toolcomponent)
for the full list and what each region is for.

---

## Adding domain + data layers

Once a tool needs to persist something or read/write through an SDK domain interactor,
introduce `data/`, `domain/`, and `di/` packages and wire a tool-scoped DI module:

```kotlin
// domain/repository/MyToolRepository.kt
internal interface MyToolRepository {
    fun setEnabled(enabled: Boolean)
    fun observeEnabled(scope: CoroutineScope): StateFlow<Boolean>
}

// data/MyToolRepositoryImpl.kt
internal class MyToolRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : MyToolRepository {
    override fun setEnabled(enabled: Boolean) = sharedPreferences.edit { putBoolean(KEY, enabled) }
    override fun observeEnabled(scope: CoroutineScope): StateFlow<Boolean> =
        sharedPreferences.observeAsStateFlow(KEY, false, scope)

    private companion object {
        const val KEY = "enabled"
    }
}

// domain/MyToolInteractor.kt
internal class MyToolInteractor(private val repository: MyToolRepository) {
    fun observeEnabled(scope: CoroutineScope) = repository.observeEnabled(scope)
    fun setEnabled(enabled: Boolean) = repository.setEnabled(enabled)
}

// di/MyToolModule.kt
internal val myToolModule = DI.Module("myToolModule") {
    // Bind the interface to its impl — Domain and UI only ever see MyToolRepository.
    bindSingleton<MyToolRepository> {
        MyToolRepositoryImpl(instance(arg = requireQualifiedName<MyToolDescriptor>()))
    }
    bindSingletonOf(::MyToolInteractor)
}
```

Then override `di` on your `AbstractTool` to extend the parent graph:

```kotlin
internal class MyTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val di: DI = subDI(super.di) { import(myToolModule) }

    override val window: ToolComponent.Window by requiredComponent { MyToolWindow() }
}
```

`instance(arg = requireQualifiedName<MyToolDescriptor>())` is the standard pattern for
obtaining your tool's isolated `SharedPreferences` instance from the parent DI graph — the
qualified name of your `ToolDescriptor` scopes the preferences file to your tool. See
[Data & domain](data-and-domain.md) and [Resource & isolation](../reference/resource-and-isolation.md)
for the full storage story (files, `SharedPreferences`, Room).

Your ViewModel then depends on the interactor, obtained through `diViewModel()`:

```kotlin
internal class MyToolViewModel(private val interactor: MyToolInteractor) : ViewModel() {
    // UiState / Action / Event — see docs/architecture/ui-layer-conventions.md
}
```

---

## Multi-screen tools and tool-scoped DI

A multi-screen tool imports its DI module once, at the `AbstractTool` level, and every
screen's ViewModel resolves its own dependencies from that same graph via `diViewModel()` —
there is no need to thread dependencies through navigation arguments. See
[UI layer conventions — multi-screen navigation](ui-layer-conventions.md#multi-screen-navigation)
for the `AppNavHost` + `Route` pattern that pairs with this.

---

## What belongs where — quick reference

| I need to…                                                             | Layer                         | Notes                                                                               |
|------------------------------------------------------------------------|-------------------------------|-------------------------------------------------------------------------------------|
| Render a screen, collect `UiState`, dispatch `Action`                  | UI                            | Composable + `diViewModel()`                                                        |
| Hold `UiState`, expose one-shot `Event`s, react to `Action`            | UI                            | ViewModel — see [UI layer conventions](ui-layer-conventions.md)                     |
| Coordinate one or more repositories / SDK interactors for a use case   | Domain                        | Interactor                                                                          |
| Read/write `SharedPreferences`, files, or a local database             | Data                          | Repository — see [Data & domain](data-and-domain.md)                                |
| Talk to an SDK domain interactor (map, model, session storage, locale) | Domain or Data, per call site | Depends whether it's business logic (domain) or raw storage (data)                  |
| Declare screen regions, DI graph, lifecycle callbacks                  | Tool                          | `AbstractTool` subclass — see [Tool lifecycle & setup](../reference/tool-lifecycle-and-setup.md) |
| Declare identity (name/icon/categories) and construct the tool         | Tool                          | `ToolDescriptor` subclass                                                           |
