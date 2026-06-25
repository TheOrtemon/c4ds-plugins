# c4ds-tool-samples Rework — Code Review Findings

Review of the implemented `:gallery` + `:isolation` rework against the SDK (`/Users/vasah/StudioProjects/c4/repos_0/c4ds`) and [`docs/samples-rework-plan.md`](samples-rework-plan.md).

**Verdict: does NOT compile.** Multiple API-misuse blockers below. The overall structure, packaging,
`combat_tools.xml` wiring, hub/catalog design, `categories` usage, string externalization, and
`values-uk`/`values-night` parity (147/147) are all correct and faithful to the plan. The problems are
concentrated in SDK API signatures that were guessed rather than verified.

Severity legend: **[BLOCKER]** compile error / will not build · **[MAJOR]** runtime/logic defect ·
**[MINOR]** convention/polish · **[VERIFY]** confirm against SDK, may already be fine.

---

## 1. BLOCKERS — build will fail

### B1. `ToolManager.activate<T>(FLAG_COMPONENT_ON_TOP)` — wrong positional arg (14 call sites)
File: `gallery/.../catalog/SampleCatalog.kt` (every same-APK `launch` lambda).

The reified overload is
`activate(packageName: String? = null, flags: Int = FLAG_NONE, …)` — a single positional arg binds to
**`packageName: String?`**, so passing an `Int` fails to compile (`type mismatch: Int vs String?`).

Fix — use the named `flags` argument:
```kotlin
launch = { mgr -> mgr.activate<WindowSimpleToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) }
```
Apply to all 14 same-APK entries. (The cross-APK entry uses `activate(it, ToolManager.FLAG_COMPONENT_ON_TOP)`
on the `(ToolId, Int, …)` member overload, which is fine.)

---

### B2. `MapTool` — three errors
File: `gallery/.../map/MapTool.kt`.

1. `private val layer: RenderableLayer by renderableMapLayer()`
   - `renderableMapLayer(isARCompatibleTool = true, isVRCompatibleTool = true, initBlock: RenderableLayer.() -> Unit)`
     has a **required** `initBlock` — `renderableMapLayer()` won't compile.
   - It returns `Lazy<Flow<RenderableLayer?>>`, so the delegated type is `Flow<RenderableLayer?>`, **not**
     `RenderableLayer`; `layer.addRenderable(...)` is invalid.
   - `AbstractMapTool` already owns a renderable layer exposed via `mapLayer` and provides
     `addRenderable(renderable)` / `removeRenderable(renderable)`. Drop the custom `layer` entirely and call
     `addRenderable(placemark)` directly.
2. `mapInteractor.requestRedraw()` — `mapInteractor` is undefined. `AbstractMapTool` does not expose it.
   Either inject `CommonMapInteractor` via the tool/VM DI, or rely on the renderable layer being marked dynamic
   (it already is) and drop the redraw call.
3. `override fun onTerrainPicked(recognizer, position, pickList)` overrides nothing. The
   `SelectDragCallback` hook implemented by `AbstractMapTool` is **`onTerrainPicked(position: Position)`**
   (see `c4ds-sdk/.../tool/AbstractMapTool.kt:77`). Use:
   ```kotlin
   override fun onTerrainPicked(position: Position) {
       super.onTerrainPicked(position)
       _lastTap.value = "${position.latitude.degrees}°, ${position.longitude.degrees}°"
       addRenderable(Placemark(position))
   }
   ```
   (Remove the `GestureRecognizer` / `PickedObjectList` params and imports.)

---

### B3. `MapWindowTool` — missing `isRequired` + out-of-scope `navigationController`
File: `gallery/.../mapwindow/MapWindowTool.kt`.

1. `mapWindow(...)` requires `isRequired: () -> Boolean` (no default). Add `isRequired = { true }`.
2. `mapEndBarButtons = { MapWindowEndBarButtons(navigationController) }` and
   `navBarContent = { MapWindowNavBar(navigationController) }` — these lambdas are
   `@Composable ColumnScope.() -> Unit` and `@Composable (() -> Unit)?` respectively; **`navigationController`
   is not in scope** (it only exists on a `MapView`, available in `initialize: MapView.() -> Unit`). This won't
   compile. Obtain the controller from the component's `mapView` at click time, e.g. capture the
   `ToolComponent.MapWindow` and read `mapView?.navigationController` inside the click handlers, or route zoom
   through the component's own update channel. Re-derive a working pattern from a first-party `MapWindow` user
   (e.g. `c4ds/c4ds-tool/optical/.../OpticalSensorMapWindow.kt`).

---

### B4. `MapController.InteractionMode.FirstPerson` does not exist
File: `gallery/.../mapwindow/ui/MapWindowNavBar.kt:20`.

The enum is `InteractionMode { LookAt, FPV, Locked }`. Use `MapController.InteractionMode.FPV`.

---

### B5. `ExpandableStatusTool` — component is not overridden
File: `gallery/.../expandablestatus/ExpandableStatusTool.kt:24`.

`private val expandableStatus by expandableStatusComponent(...)` collides with the inherited
`open val expandableStatus: ToolComponent.ExpandableStatus?` → compile error ("hides member of supertype and
needs 'override' modifier"). It must be:
```kotlin
override val expandableStatus: ToolComponent.ExpandableStatus by expandableStatusComponent(...) { ... }
```
Without `override` the host would also never render the component. (For reference, `StatusTool` correctly uses
`override val status`.)

---

### B6. `ServiceSampleService` — wrong super-ctor, non-existent lifecycle hooks, wrong scope name
Files: `gallery/.../service/ServiceSampleService.kt`, `gallery/.../service/ServiceTool.kt`.

`AbstractToolService`'s constructor is
`AbstractToolService(baseContext: ToolContext, descriptor: ToolDescriptor, parentDI: DI)`
and it exposes `protected val coroutineScope`. There are **no** `onStart()` / `onStop()` hooks (only
`onConfigurationChanged` and `onDestroy`).

Current code: `: AbstractToolService(toolContext, di)` (2 args, `di` bound to `descriptor`), overrides
`onStart`/`onStop` (override nothing), and uses `scope.launch` (`scope` undefined). All compile errors.

Fix:
```kotlin
internal class ServiceSampleService(
    toolContext: ToolContext,
    descriptor: ToolDescriptor,
    di: DI,
    private val sharedState: ServiceSharedState,
) : AbstractToolService(toolContext, descriptor, di) {

    init {
        // No onStart hook — the service runs from construction; cancel happens in onDestroy().
        coroutineScope.launch {
            while (isActive) {
                delay(TICK_INTERVAL_MS)
                sharedState.incrementEvent(SimpleDateFormat("HH:mm:ss", Locale.US).format(Date()))
            }
        }
    }

    override fun onDestroy() { Log.i(TAG, "ServiceSampleService destroyed") }
}
```
And in `ServiceToolDescriptor.createService`, pass the descriptor:
```kotlin
override fun createService(toolContext: ToolContext, di: DI) =
    ServiceSampleService(toolContext, this, di, sharedState)
```
Also drop the `service_running` / `service_stopped` strings or wire a real state, since there is no
start/stop lifecycle to reflect.

---

### B7. `ToolDialog.Custom` misused
File: `gallery/.../dialog/DialogWindow.kt:64`.

`ToolDialog.Custom(header: @Composable DialogHeaderScope.() -> Unit, content: …? = null, buttons: @Composable DialogButtonsScope.() -> Unit)`
has **no `title`** parameter and **requires `header` and `buttons`**. Current call
`ToolDialog.Custom(title = …, content = …)` won't compile. Provide `header`/`buttons` lambdas, e.g.:
```kotlin
ToolDialog.Custom(
    header = { /* DialogHeaderScope content, e.g. title text */ },
    content = { Text(context.getString(R.string.dialog_custom_body)) },
    buttons = { /* DialogButtonsScope: confirm/dismiss buttons calling onDismissDialog() */ },
)
```
Inspect `c4ds-sdk-core/ui/.../component/dialog/` for `DialogHeaderScope` / `DialogButtonsScope` members.

---

### B8. `ModelViewModel` — `StateFlow` used as `Boolean`, `Sequence` used as `Flow`, `createModel()` needs args
File: `gallery/.../model/ModelViewModel.kt`.

1. `CommonModelInteractor.isReadOnly` is `StateFlow<Boolean>`. Used directly where `Boolean` is required at
   lines 24, 53, 61. Use `.value` (or collect it into UiState):
   ```kotlin
   UiState(isReadOnly = modelInteractor.isReadOnly.value)
   if (modelInteractor.isReadOnly.value) return
   ```
2. `getAllModels(): Sequence<IdentifiableModel>` is a one-shot snapshot, not a `Flow` — `.launchIn(...)`
   doesn't exist on `Sequence` and `.onEach { list -> … }` iterates elements, not a list. Read it as a list
   and refresh on `modelsCollectionUpdatedEvent` / `modelUpdatedEvent`:
   ```kotlin
   modelInteractor.modelsCollectionUpdatedEvent
       .onStart { emit(Unit) }
       .onEach { _uiState.update { it.copy(allModels = modelInteractor.getAllModels()
           .filterIsInstance<BattlespaceConceptModel>().toList()) } }
       .launchIn(viewModelScope)
   ```
3. `createModel()` is `createModel(startPoint: GeoPoint, modelAttrs: ModelAttrs, pointShifter: PointShifter, templated: Boolean = true)`
   — the no-arg call won't compile. Supply a start point (e.g. from `modelInteractor.cursorPoint.value` or
   `CommonMapInteractor.selectedPosition`) and a `ModelAttrs`. If a correct minimal `createModel` call proves
   too involved for a sample, prefer demonstrating select/unselect/observe only and document create/delete as
   advanced (update the plan/README accordingly rather than ship a broken call).

---

## 2. MAJOR — logic / faithfulness

### M1. `MapTool` placemarks may never render
Tied to B2: because the sample creates its own `renderableMapLayer` instead of using `AbstractMapTool`'s
managed layer (surfaced via `mapLayer`), tapped placemarks would not appear even if it compiled. Using
`addRenderable(...)` (B2) resolves this.

### M2. Hardcoded English strings (violates the plan's i18n rule)
- `gallery/.../window/simple/WindowSimpleWindow.kt`: `label = "Speed"`, `"Altitude"`, `"Azimuth"` (lines
  124/130/136). Add string resources (+`values-uk`).
- `gallery/.../resources/material/ui/MaterialWindow.kt`: `"Open DropdownMenu"`, `"OK"`,
  `"Snackbar from plugin — host R resolved OK"`, `"Alpha"/"Beta"/"Gamma"`. Externalize (these are visible UI
  text). The plan states "No hardcoded English text in any `.kt` file."

### M3. `WindowSimpleWindow` distance input mislabeled
`DistanceInput(... label = stringResource(R.string.window_simple_coordinates))` labels a distance field
"Coordinates". Add a dedicated `window_simple_distance` string.

---

## 3. MINOR / NITS

### N1. Inconsistent descriptor/tool file layout
The plan specifies a separate `<Area>ToolDescriptor.kt` per sample, but the implementation is split:
- Separate files: `status`, `map`, `overlay`, `mapwindow`, `window/simple`, `window/navigation`.
- Co-located (descriptor + tool in one file): `catalog`, `dialog`, `endbar`, `model`, `service`, `underlay`,
  `resources/config`, `resources/material`, `resources/collision`.

Co-locating is acceptable for small samples, but pick one convention repo-wide. (FQCNs in `combat_tools.xml`
all resolve either way, so this is not a build issue.)

### N2. `endbar` sample action is a no-op
`EndBarActionButton(onClick = { /* action no-op in sample */ })` — wire it to a toast
(`endbar_action_fired` already exists) so the action button visibly does something.

### N3. Service strings imply a state that doesn't exist
`service_running` / `service_stopped` are unused-by-design once B6 is applied (no start/stop lifecycle).
Reflect the real signal (event count + last event time from `ServiceSharedState`) instead.

---

## 4. VERIFY (likely fine — confirm against SDK to avoid surprises)

- **V1. EndBar menu API names.** `gallery/.../endbar/EndBarSampleTool.kt` uses
  `EndBarMenuButton(icon, contentDescription) { Checkable(label = …); Slider(label = …, valueRange = …) }`.
  Per `c4ds-sdk-core/ui/.../component/bar/endbar/EndBar.kt`:
  - `EndBarMenuButton(icon, title: String, contentDescription = "", …)` — **`title` is required** (no default);
    the current call omits it → **this is actually a BLOCKER**, add `title = …`.
  - `EndBarMenuScope.Checkable(title: String, …)` and `Slider(title: String, …, range: ClosedFloatingPointRange<Float>, …)`
    — parameter names are **`title`** and **`range`**, not `label`/`valueRange` → **BLOCKER**, rename.
  (Listed here because it sits next to the other endbar code, but treat V1 as build-blocking.)
- **V2. Button signatures.** Confirm `Button`/`TextButton`/`DestructiveButton`
  (`c4ds-sdk-core/ui/.../component/button/`) accept `label: String`, `onClick`, and `enabled`. Used pervasively
  (catalog, model, dialog, window-simple, mapwindow). If the param is not `label`, many files break.
- **V3. Measurement/coordinate input param names.** Confirm `DistanceInput(distanceMetres=…)`,
  `SpeedInput(speedMps=…)`, `AltitudeInput(altitudeMetres=…)`, `AngleInput(angleDegrees=…)`,
  `CoordinatesInputWithSystem(location=…, onLocationChanged=…, enabled=…)`.
- **V4. Kodein-compose on the plugin classpath.** `CatalogListScreen` uses
  `org.kodein.di.compose.rememberInstance<ToolManager>()`. Confirm `kodein-di-framework-compose` is reachable
  transitively from `c4ds-sdk`; if not, obtain `ToolManager` another way (e.g. inject into a ViewModel via
  `diViewModel()`).
- **V5. `OverlaySampleViewModel` domain accessors.** Confirm `PersonModel.location?.center`, `GeoPoint.toLocation()`
  (`c4ds-sdk-core/domain/.../util`), and `Location.toString(CoordinateSystemFormat)` /
  `Position.toString(CoordinateSystemFormat)` (`c4ds-sdk-core/ui/.../util`) exist with those shapes.
- **V6. `WindowNavRepository.observeAsStateFlow`.** Confirm
  `vision.combat.c4.ds.sdk.data.util.observeAsStateFlow(key, default, scope)` is exported by `:c4ds-sdk-core:data`.
- **V7. ViewModel constructor injection.** `ModelViewModel`/`OverlaySampleViewModel`/`WindowSimpleViewModel`
  rely on `diViewModel()` resolving constructor interactors from the tool DI graph. Confirm these VM types are
  bound (or that `DiViewModelFactory` performs constructor injection) — otherwise they fail at runtime.

---

## 5. Suggested fix order

1. Build blockers B1–B8 + V1 (rename endbar menu params / add `title`).
2. Re-run `./gradlew :gallery:assembleRelease :isolation:assembleRelease` (creds in `~/.gradle/gradle.properties`).
3. Resolve the VERIFY items surfaced by the compiler.
4. M2/M3 i18n + labels, then N1–N3 polish.
5. Re-verify per the plan's §10 smoke-test matrix.

## 6. What's already correct (no action)

- Module split, namespaces/`applicationId`, `settings.gradle.kts`, `libs.versions.toml`
  (`combat-ds-sdk-runtime` typo fixed), manifests, and per-APK `combat_tools.xml` FQCNs.
- `nativelib` package name (Java-keyword issue avoided), JNI/asset isolation port, `onComponentShown(component)`
  override, cross-APK `resolveToolId` + grayed-out card logic.
- Hub/catalog structure, `SampleSection` grouping, `categories = emptyList()` on all non-hub descriptors with
  `CATEGORY_LAUNCHER` only on `CatalogToolDescriptor`.
- String externalization and `values-uk` (147/147), `values-night`/`values-night-uk` overrides, drawables.
