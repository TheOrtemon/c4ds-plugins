# c4ds-tool-samples — Structured Sample Suite Rework Plan

**Implementer instructions:** This document is a self-contained specification. Execute it top to bottom.
Read the referenced SDK source paths when you need exact signatures. Do NOT modify this file; it is the plan of record.

---

## Table of contents

1. [Goals and scope](#1-goals-and-scope)
2. [Confirmed decisions](#2-confirmed-decisions)
3. [Target repository layout](#3-target-repository-layout)
4. [Hub tool — CatalogTool](#4-hub-tool--catalogtool)
5. [Gallery sample specs (`:gallery` APK)](#5-gallery-sample-specs-gallery-apk)
6. [Isolation APK specs (`:isolation`)](#6-isolation-apk-specs-isolation)
7. [Conventions](#7-conventions)
8. [Migration map — current → target](#8-migration-map--current--target)
9. [README rewrite outline](#9-readme-rewrite-outline)
10. [Build and verification steps](#10-build-and-verification-steps)

---

## 1. Goals and scope

### Problem with the current samples

The existing two APKs (`:overlay`, `:window`) have these structural problems:

- Inconsistent naming: Kotlin packages use `vision.combat.c4.ds.example.tool.*` but Android namespace uses `vision.combat.c4.ds.tool.sample.*`.
- Pedagogical examples mixed with isolation QA tools inside the same APK with no clear grouping.
- Missing coverage: `Status`, `ExpandableStatus`, `Underlay`, `MapWindow`, `AbstractMapTool`, `AbstractToolService`, `ToolNotificationManager`, `ToolDialog`, `categories` usage, `autoStart`, `ToolParams` deep links.
- All tools appear in the host launcher, cluttering it.
- Hardcoded English strings in navigation/simple windows — no `values-uk` coverage.
- No hub / catalog tool giving developers a guided entry point.

### Goals

1. Replace the two existing APKs with two new ones — `:gallery` and `:isolation` — with a unified package root.
2. Provide a hub (`CatalogTool`) that is the **only** launcher-visible tool. Every sample is launched from the hub, which also serves as the `categories` API demo.
3. Cover every major SDK surface, ordered from simple to complex inside a `now-in-Android`-style catalog UI.
4. All user-facing text in `strings.xml` + `values-uk`. No hardcoded English.
5. Every `ToolDescriptor` self-documents its purpose in a KDoc header.

### Out of scope

- No changes to the host app or the SDK.
- No shared library module — each sample is a self-contained, copy-pasteable subpackage.
- No CI/CD changes.
- Case (d) — pinned state surviving `versionCode` bump — remains a manual procedure documented in the README (not an automated test or dedicated tool), because it requires two separate installs.

---

## 2. Confirmed decisions

| Decision | Choice | Rationale |
|---|---|---|
| Module layout | `:gallery` APK + `:isolation` APK | Separate package needed to exercise per-APK classloader and resource isolation; `:gallery` covers all feature scenarios within one APK |
| Launcher visibility | Only `CatalogTool` has `CATEGORY_LAUNCHER`; all other descriptors use `categories = emptyList()` | Keeps host launcher clean; doubles as the `categories` API demo; precedent: `ShareTacticalDataToolDescriptor` in first-party tools |
| Package root | `vision.combat.c4.ds.sample.*` | Eliminates the current `example.tool` vs `tool.sample` split |
| Cross-APK launch | Hub calls `ToolManager.resolveToolId(fqcn)` for the `:isolation` target | The descriptor class is not on the gallery classpath, so reified `ToolId<T>()` cannot be used |

---

## 3. Target repository layout

### `settings.gradle.kts` change

```kotlin
// Replace:
include(":overlay")
include(":window")

// With:
include(":gallery")
include(":isolation")
```

### `:gallery` APK directory tree

```
gallery/
├── build.gradle.kts                   # (see §3.1)
├── proguard-rules.pro
└── src/main/
    ├── AndroidManifest.xml
    ├── res/
    │   ├── drawable/                  # ic_catalog.xml, ic_window.xml, ic_overlay.xml,
    │   │                              # ic_map.xml, ic_mapwindow.xml, ic_status.xml,
    │   │                              # ic_expandable_status.xml, ic_underlay.xml,
    │   │                              # ic_end_bar.xml, ic_model.xml, ic_service.xml,
    │   │                              # ic_resources.xml, ic_material.xml, ic_collision.xml,
    │   │                              # ic_dialog.xml, ic_launcher_foreground.xml
    │   ├── drawable-night/            # ic_resources.xml (night variant for resources sample)
    │   ├── font/                      # sample_font.ttf (copy from current :window)
    │   ├── raw/                       # sample_note.txt (copy from current :window)
    │   ├── mipmap-{mdpi..xxxhdpi}/    # ic_launcher.png
    │   ├── mipmap-anydpi-v26/         # ic_launcher.xml
    │   ├── values/
    │   │   ├── strings.xml            # all sample strings (see §7 conventions)
    │   │   └── ic_launcher_background.xml
    │   ├── values-uk/strings.xml
    │   ├── values-night/strings.xml   # config_mode = "NIGHT mode active"
    │   ├── values-night-uk/strings.xml # config_mode = "НІЧНИЙ режим (uk)"
    │   └── xml/combat_tools.xml       # all 14 gallery descriptors
    └── kotlin/vision/combat/c4/ds/sample/gallery/
        ├── catalog/                   # CatalogToolDescriptor, CatalogTool, ...
        ├── window/
        │   ├── simple/
        │   └── navigation/
        ├── dialog/
        ├── overlay/
        ├── map/
        ├── mapwindow/
        ├── status/
        ├── expandablestatus/
        ├── underlay/
        ├── endbar/
        ├── model/
        ├── service/
        └── resources/
            ├── config/
            ├── material/
            └── collision/
```

### `:isolation` APK directory tree

```
isolation/
├── build.gradle.kts                   # (see §3.2)
├── proguard-rules.pro
└── src/main/
    ├── AndroidManifest.xml
    ├── assets/isolation/sample.txt
    ├── cpp/
    │   ├── CMakeLists.txt
    │   └── isolation_jni.cpp
    ├── res/
    │   ├── drawable/ic_isolation.xml, ic_launcher_foreground.xml
    │   ├── mipmap-{mdpi..xxxhdpi}/ic_launcher.png
    │   ├── mipmap-anydpi-v26/ic_launcher.xml
    │   ├── values/strings.xml
    │   ├── values-uk/strings.xml
    │   └── xml/combat_tools.xml       # 1 descriptor
    └── kotlin/vision/combat/c4/ds/sample/isolation/
        └── nativelib/                 # NOT "native" — that is a reserved Java keyword and is illegal in a package segment
            ├── NativeToolDescriptor.kt
            ├── NativeTool.kt
            ├── NativeToolWindow.kt
            └── IsolationNative.kt
```

### 3.1 `:gallery` `build.gradle.kts`

Copy the current `:window/build.gradle.kts` as the baseline (no NDK/CMake needed) and change:

```kotlin
android {
    namespace = "vision.combat.c4.ds.sample.gallery"
    defaultConfig {
        applicationId = "vision.combat.c4.ds.sample.gallery"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }
}
```

### 3.2 `:isolation` `build.gradle.kts`

Copy the current `:overlay/build.gradle.kts` and change:

```kotlin
android {
    namespace = "vision.combat.c4.ds.sample.isolation"
    defaultConfig {
        applicationId = "vision.combat.c4.ds.sample.isolation"
        // keep ndk { abiFilters += listOf("arm64-v8a", "x86_64") }
    }
    // keep externalNativeBuild { cmake { path = ... } }
    // keep packaging { jniLibs { useLegacyPackaging = true } }
}
```

### Manifest pattern (same for both APKs)

```xml
<application
    android:label="@string/app_name"
    android:icon="@mipmap/ic_launcher"
    android:theme="@style/Theme.AppCompat.NoActionBar"
    android:hasCode="false">  <!-- no Activity/Service declared by the plugin APK itself -->
    <meta-data
        android:name="vision.combat.c4.ds.sdk.DECLARED_TOOLS"
        android:resource="@xml/combat_tools" />
</application>
```

### `combat_tools.xml` for `:gallery`

```xml
<?xml version="1.0" encoding="utf-8"?>
<combat-tools>
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.catalog.CatalogToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.window.simple.WindowSimpleToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.window.navigation.WindowNavToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.dialog.DialogToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.overlay.OverlaySampleToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.map.MapToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.mapwindow.MapWindowToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.status.StatusToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.expandablestatus.ExpandableStatusToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.underlay.UnderlayToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.endbar.EndBarSampleToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.model.ModelToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.service.ServiceToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.resources.config.ConfigToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.resources.material.MaterialToolDescriptor" />
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.resources.collision.CollisionToolDescriptor" />
</combat-tools>
```

### `combat_tools.xml` for `:isolation`

```xml
<?xml version="1.0" encoding="utf-8"?>
<combat-tools>
    <tool-descriptor name="vision.combat.c4.ds.sample.isolation.nativelib.NativeToolDescriptor" />
</combat-tools>
```

---

## 4. Hub tool — CatalogTool

### Purpose

`CatalogTool` is the **only** tool that appears in the host launcher. It is a `Window`-based tool whose UI is a grouped catalog of all samples. Tapping a sample card launches the corresponding tool. There is also a detail screen per sample showing which SDK API it demonstrates and where the source lives.

### Files to create

```
catalog/
├── CatalogToolDescriptor.kt
├── CatalogTool.kt
├── SampleCatalog.kt          # data model + registry
├── SampleSection.kt          # enum: WINDOWS, MAP, STATUS_AND_BARS, MODEL_AND_LIFECYCLE, RESOURCES_AND_ISOLATION
├── SampleEntry.kt            # data class
└── ui/
    ├── CatalogWindow.kt      # AppNavHost root
    ├── CatalogListScreen.kt  # LazyColumn with section headers and SampleCard composable
    └── CatalogDetailScreen.kt
```

### `CatalogToolDescriptor`

```kotlin
/**
 * Hub tool — the only launcher-visible entry point.
 *
 * All other sample tools set [categories] = emptyList() and are launched
 * from this catalog via [ToolManager]. This descriptor demonstrates:
 *   - [ToolDescriptor.categories] — CATEGORY_LAUNCHER controls host launcher visibility
 *   - [ToolDescriptor.CATEGORY_LAUNCHER] constant
 *
 * SDK files: c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolDescriptor.kt
 */
class CatalogToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.catalog_tool_name
    override val iconResId: Int = R.drawable.ic_catalog
    // categories = listOf(CATEGORY_LAUNCHER) is the default — explicit here for documentation
    override val categories: List<String> = listOf(CATEGORY_LAUNCHER)
    override fun createTool(...) = CatalogTool(toolContext, this, di, params)
}
```

### `SampleEntry` and `SampleSection`

```kotlin
enum class SampleSection(@StringRes val titleResId: Int) {
    WINDOWS(R.string.section_windows),
    MAP(R.string.section_map),
    STATUS_AND_BARS(R.string.section_status_bars),
    MODEL_AND_LIFECYCLE(R.string.section_model_lifecycle),
    RESOURCES_AND_ISOLATION(R.string.section_resources_isolation),
}

data class SampleEntry(
    val section: SampleSection,
    @StringRes val nameResId: Int,
    @StringRes val descResId: Int,
    @StringRes val apisResId: Int,   // which SDK APIs: shown on detail screen
    val sourceSubpackage: String,    // e.g. "window/simple" shown on detail screen
    val launch: (ToolManager, packageName: String) -> Unit,
)
```

### `SampleCatalog` — same-APK activation

For every gallery sample the catalog uses the reified overload:

```kotlin
// SDK: c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolManager.kt
// inline fun <reified T : ToolDescriptor> ToolManager.activate(flags: Int = FLAG_NONE, ...)

launch = { mgr, pkg ->
    mgr.activate<WindowSimpleToolDescriptor>(FLAG_COMPONENT_ON_TOP)
}
```

### `SampleCatalog` — cross-APK activation for `:isolation`

The `NativeToolDescriptor` class is not on the gallery classpath, so use FQCN resolution:

```kotlin
// SDK: ToolManager.resolveToolId(className: String): ToolId?
private const val ISOLATION_NATIVE_FQCN =
    "vision.combat.c4.ds.sample.isolation.nativelib.NativeToolDescriptor"

launch = { mgr, _ ->
    mgr.resolveToolId(ISOLATION_NATIVE_FQCN)
        ?.let { mgr.activate(it, FLAG_COMPONENT_ON_TOP) }
}
```

> **Note:** `resolveToolId` returns `null` if the `:isolation` APK is not installed. The catalog list screen should show a disabled/grayed card for isolation entries when `resolveToolId` returns null (check on each recomposition via `ToolManager.resolveToolId`).

### `CatalogTool` body

```kotlin
internal class CatalogTool(...) : AbstractTool(...) {
    override val window: ToolComponent.Window by requiredComponent {
        CatalogWindow()
    }
}
```

### `CatalogWindow` UI structure

```
CatalogWindow
└── AppNavHost(startDestination = "list")
    ├── composable("list") → CatalogListScreen
    └── composable("detail/{sampleId}") → CatalogDetailScreen
```

`CatalogListScreen`:
- `WindowScaffold` + `BackNavTopAppBar(title = stringResource(catalog_tool_name))`
- `LazyColumn` with sticky section headers (`SampleSection` titles)
- Per entry: `SampleCard(name, desc, onLaunch, onDetails)` with a **Launch** and **Details** button
- `Launch` button calls `entry.launch(toolManager, packageName)` then `navController` stays on list
- `Details` button navigates to `detail/{sampleId}`

`CatalogDetailScreen`:
- `BackNavTopAppBar` with back navigation
- Shows: sample name, description, SDK APIs used (from `apisResId`), source subpackage path

### Inject `ToolManager`

```kotlin
// Inside CatalogWindow (Compose context):
val toolManager: ToolManager = rememberInstance()  // Kodein, SDK DI graph
```

---

## 5. Gallery sample specs (`:gallery` APK)

Each section below specifies one sample tool. The sample is a self-contained subpackage. File names follow the `<Area>Tool*` convention.

---

### 5.1 Window — Simple (`window/simple/`)

**Purpose:** Minimum viable window tool with SDK form inputs and model interactor.

**SDK APIs demonstrated:**
- `ToolComponent.Window`, `requiredComponent { ... }` — `c4ds-sdk/.../sdk/tool/AbstractTool.kt`
- `WindowScaffold`, `BackNavTopAppBar` — `c4ds-sdk-core/ui/.../ui/component/WindowScaffold.kt`, `TopAppBar.kt`
- `DistanceInput`, `SpeedInput`, `AltitudeInput`, `AngleInput`, `CoordinatesInputWithSystem` — `c4ds-sdk-core/ui/.../ui/component/`
- `CommonModelInteractor.selectedModel`, `.userModel`, `.unselectModel()` — `c4ds-sdk-core/domain/.../interactor/CommonModelInteractor.kt`
- `diViewModel()` — `c4ds-sdk/.../sdk/tool/ToolViewModel.kt`
- `Context.showToast(...)` — `c4ds-sdk-core/ui/.../ui/util/Toast.kt`

**Files to create:**

```
window/simple/
├── WindowSimpleToolDescriptor.kt
├── WindowSimpleTool.kt
├── WindowSimpleViewModel.kt    # diViewModel(); collects selectedModel, userModel
└── WindowSimpleWindow.kt       # WindowScaffold + form inputs + model section
```

**`WindowSimpleToolDescriptor`:**

```kotlin
/**
 * Demonstrates the minimum viable window tool.
 *
 * SDK APIs: ToolComponent.Window, WindowScaffold, BackNavTopAppBar,
 *           SDK measurement inputs (Distance/Speed/Altitude/Angle/Coordinates),
 *           CommonModelInteractor (selectedModel, userModel, unselectModel).
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractTool.kt
 *   c4ds-sdk-core/ui/src/main/kotlin/vision/combat/c4/ds/sdk/ui/component/WindowScaffold.kt
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonModelInteractor.kt
 */
class WindowSimpleToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.window_simple_tool_name
    override val iconResId = R.drawable.ic_window
    override val categories: List<String> = emptyList()
    override fun createTool(...) = WindowSimpleTool(...)
}
```

**Resources:**
- Strings: `window_simple_tool_name`, `window_simple_section_model`, `window_simple_section_inputs`, `window_simple_user_model`, `window_simple_selected_model`, `window_simple_model_name`, `window_simple_coordinates`, `window_simple_not_selected`, `window_simple_unselect`, `window_simple_unselected_toast` (+ `values-uk` equivalents)

**Verification:** Open from catalog → window appears → form inputs accept values → model section shows current selected/user model → Unselect button fires toast.

---

### 5.2 Window — Navigation (`window/navigation/`)

**Purpose:** Multi-screen window using `AppNavHost`, tool-scoped DI module, and tool-scoped `SharedPreferences`. Shows how to navigate between screens and persist per-tool settings.

**SDK APIs demonstrated:**
- `AppNavHost`, `Route`, `NavExitTransition`, `NavEnterTransition` — `c4ds-sdk-core/ui/.../ui/navigation/AppNavHost.kt`, `Route.kt`
- `BackNavigationButton()`, `BackNavTopAppBar` — `c4ds-sdk-core/ui/.../ui/component/bar/TopAppBar.kt`
- `subDI { import(module) }` — Kodein tool child graph (existing pattern in `NavigationTool.kt`)
- `ToolManager.activate<T>(FLAG_COMPONENT_ON_TOP)` — cross-tool activation to `WindowSimpleTool`

**Files to create:**

```
window/navigation/
├── WindowNavToolDescriptor.kt
├── WindowNavTool.kt            # subDI { import(windowNavModule) }; window by requiredComponent
├── WindowNavRoute.kt           # sealed interface: Home, Settings
├── di/
│   └── WindowNavModule.kt      # Kodein module: bind SharedPreferences instance
├── data/
│   └── WindowNavRepository.kt  # observeAsStateFlow for persisted toggle
└── ui/
    ├── WindowNavWindow.kt       # AppNavHost
    ├── HomeScreen.kt            # tool info, "Go to Settings" button, "Launch Window Simple" button
    └── SettingsScreen.kt        # toggle: "Open Simple on top"
```

**`WindowNavToolDescriptor`:**

```kotlin
/**
 * Demonstrates multi-screen window navigation.
 *
 * SDK APIs: AppNavHost, Route, BackNavigationButton, subDI + Kodein module,
 *           ToolManager.activate (cross-tool, same APK).
 *
 * SDK files:
 *   c4ds-sdk-core/ui/.../navigation/AppNavHost.kt
 *   c4ds-sdk-core/ui/.../navigation/Route.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolManager.kt
 */
class WindowNavToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.window_nav_tool_name
    override val iconResId = R.drawable.ic_window
    override val categories: List<String> = emptyList()
    override fun createTool(...) = WindowNavTool(...)
}
```

**Resources:**
- Strings: `window_nav_tool_name`, `window_nav_home_title`, `window_nav_home_desc`, `window_nav_go_settings`, `window_nav_launch_simple`, `window_nav_settings_title`, `window_nav_settings_open_on_top` (+ uk)

**Verification:** Open from catalog → Home screen shown → navigate to Settings → toggle persists after closing and reopening → "Launch Window Simple" opens `WindowSimpleTool` on top.

---

### 5.3 Dialog (`dialog/`)

**Purpose:** Demonstrates all four `ToolDialog` variants using `AbstractTool.showDialog` / `dismissDialog`.

**SDK APIs demonstrated:**
- `ToolDialog.Confirmation`, `.Destructive`, `.Info`, `.Custom` — `c4ds-sdk/.../sdk/tool/ToolDialog.kt`
- `AbstractTool.showDialog(dialog)`, `AbstractTool.dismissDialog()` — `AbstractTool.kt`
- `WindowScaffold`, `TextButton`, `DestructiveButton` — SDK UI components

**Files to create:**

```
dialog/
├── DialogToolDescriptor.kt
├── DialogTool.kt               # window by requiredComponent; exposes showDialog/dismissDialog
└── DialogWindow.kt             # four buttons, one per ToolDialog type
```

**`DialogToolDescriptor`:**

```kotlin
/**
 * Demonstrates all four ToolDialog types.
 *
 * SDK APIs: ToolDialog.Confirmation, .Destructive, .Info, .Custom,
 *           AbstractTool.showDialog(), AbstractTool.dismissDialog().
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolDialog.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractTool.kt
 */
class DialogToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.dialog_tool_name
    override val iconResId = R.drawable.ic_dialog
    override val categories: List<String> = emptyList()
    override fun createTool(...) = DialogTool(...)
}
```

**`DialogTool` pattern:**

```kotlin
// Access the tool reference from within Compose:
val tool = LocalToolOwner.current  // or pass via lambda captured from AbstractTool scope
// Show:
tool.showDialog(ToolDialog.Confirmation(
    title = "Confirm action",
    body = "Are you sure?",
    confirmLabel = "Yes",
    dismissLabel = "Cancel",
    onConfirm = { /* ... */ },
))
```

> Inspect `ToolDialog.kt` at `c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolDialog.kt` for exact constructor parameters; they vary per variant.

**Resources:**
- Strings: `dialog_tool_name`, `dialog_show_confirmation`, `dialog_show_destructive`, `dialog_show_info`, `dialog_show_custom`, `dialog_confirm_title`, `dialog_confirm_body`, `dialog_confirm_yes`, `dialog_confirm_cancel`, `dialog_destructive_title`, `dialog_destructive_body`, `dialog_info_title`, `dialog_info_body`, `dialog_custom_title` (+ uk)

**Verification:** Open from catalog → four buttons visible → each shows the correct dialog type → confirm/dismiss works.

---

### 5.4 Overlay (`overlay/`)

**Purpose:** Map overlay composable reading cursor position and user model.

**SDK APIs demonstrated:**
- `ToolComponent.Overlay`, `requiredComponent`, `component` — `c4ds-sdk/.../sdk/tool/ToolComponent.kt`
- `CommonMapInteractor.selectedPosition` — `c4ds-sdk-core/domain/.../interactor/CommonMapInteractor.kt`
- `CommonModelInteractor.userModel` — `CommonModelInteractor.kt`
- `CommonLocaleSettingsInteractor.coordinateSystemFormat` — `c4ds-sdk-core/domain/.../interactor/settings/CommonLocaleSettingsInteractor.kt`
- SDK overlay theme tokens (`primaryOverlay`, `mediumOverlay`) — `c4ds-sdk-core/ui/.../ui/theme/`

**Files to create:**

```
overlay/
├── OverlaySampleToolDescriptor.kt
├── OverlaySampleTool.kt        # overlay by requiredComponent { OverlaySampleOverlay() }
├── OverlaySampleViewModel.kt   # diViewModel(); selectedPosition, userModel StateFlows
└── OverlaySampleOverlay.kt     # Composable map overlay UI
```

**`OverlaySampleToolDescriptor`:**

```kotlin
/**
 * Demonstrates ToolComponent.Overlay with map and model interactors.
 *
 * SDK APIs: ToolComponent.Overlay, CommonMapInteractor.selectedPosition,
 *           CommonModelInteractor.userModel, CommonLocaleSettingsInteractor.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonMapInteractor.kt
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonModelInteractor.kt
 */
class OverlaySampleToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.overlay_tool_name
    override val iconResId = R.drawable.ic_overlay
    override val categories: List<String> = emptyList()
    override fun createTool(...) = OverlaySampleTool(...)
}
```

**Resources:**
- Strings: `overlay_tool_name`, `overlay_cursor_position`, `overlay_user_model`, `overlay_not_available` (+ uk)

**Verification:** Open from catalog → overlay appears on map → cursor position updates as map moves → user model name appears when present.

---

### 5.5 Map (`map/`)

**Purpose:** Map tap callbacks and renderable layer drawing using `AbstractMapTool`.

**SDK APIs demonstrated:**
- `AbstractMapTool` (extends `AbstractTool`, adds `SelectDragCallback`) — `c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractMapTool.kt`
- `addRenderable(renderable)`, `removeRenderable(renderable)`, `mapLayer: Flow<RenderableLayer>` — `AbstractMapTool.kt`
- `SelectDragCallback` `onTerrainPicked`, `onObjectPicked`, `onNothingPicked` — WorldWind interfaces (transitive via SDK)
- `CommonMapInteractor.focusOnLocation`, `.requestRedraw()`, `.selectedPosition` — `CommonMapInteractor.kt`
- `ToolComponent.Status` (mini status bar showing tap coordinates) — `ToolComponent.kt`
- `renderableMapLayer { }` factory helper — `c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolLayer.kt`

**Files to create:**

```
map/
├── MapToolDescriptor.kt
├── MapTool.kt                  # extends AbstractMapTool; onTerrainPicked adds Placemark; status shows last tap
└── ui/
    ├── MapStatusBar.kt         # ToolComponent.Status content: last-tapped coords
    └── MapWindowHint.kt        # optional small window with "tap map to place marker" hint
```

**`MapToolDescriptor`:**

```kotlin
/**
 * Demonstrates AbstractMapTool: renderable layers, SelectDragCallback, map interaction.
 *
 * SDK APIs: AbstractMapTool, SelectDragCallback, addRenderable, RenderableLayer,
 *           ToolComponent.Status, CommonMapInteractor.focusOnLocation.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractMapTool.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolLayer.kt
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonMapInteractor.kt
 */
class MapToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.map_tool_name
    override val iconResId = R.drawable.ic_map
    override val categories: List<String> = emptyList()
    override fun createTool(...) = MapTool(...)
}
```

**`MapTool` sketch:**

```kotlin
internal class MapTool(...) : AbstractMapTool(toolContext, descriptor, parentDI, params) {
    private val layer: RenderableLayer by renderableMapLayer(...)

    override val status: ToolComponent.Status by statusComponent {
        MapStatusBar(viewModel)
    }

    override fun onTerrainPicked(recognizer: GestureRecognizer, position: Position, pickList: PickedObjectList) {
        super.onTerrainPicked(recognizer, position, pickList)
        viewModel.onTerrainTapped(position)
        val placemark = Placemark(position).apply { /* configure */ }
        addRenderable(placemark)
        mapInteractor.requestRedraw()
    }
}
```

**Resources:**
- Strings: `map_tool_name`, `map_status_tap_hint`, `map_last_tap`, `map_no_tap_yet` (+ uk)

**Verification:** Open from catalog → tap map terrain → placemark appears → status bar updates with coordinates.

---

### 5.6 MapWindow (`mapwindow/`)

**Purpose:** Embedded secondary map inside a tool window via `ToolComponent.MapWindow`.

**SDK APIs demonstrated:**
- `ToolComponent.MapWindow`, `mapWindow { }` factory — `c4ds-sdk/.../sdk/tool/ToolComponent.kt`
- `MapWindow.mapEndBarButtons`, `MapWindow.navBarContent`, `MapWindow.focusCameraOn` — `ToolComponent.kt`
- `MapView`, `MapController.InteractionMode`, `MapController.setLookAt` — `c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/map/MapView.kt`, `MapController.kt`
- `MapHost` — `c4ds-sdk/.../sdk/map/MapHost.kt`

**Files to create:**

```
mapwindow/
├── MapWindowToolDescriptor.kt
├── MapWindowTool.kt            # window = mapWindow { ... showMapOnActivation = true }
└── ui/
    ├── MapWindowEndBarButtons.kt  # mapEndBarButtons content: zoom in/out
    └── MapWindowNavBar.kt         # navBarContent: mode selector (LookAt/FPV)
```

**`MapWindowToolDescriptor`:**

```kotlin
/**
 * Demonstrates ToolComponent.MapWindow with embedded map controls.
 *
 * SDK APIs: ToolComponent.MapWindow, mapWindow { } factory, MapView, MapController,
 *           MapWindow.mapEndBarButtons, MapWindow.navBarContent,
 *           MapController.InteractionMode, MapController.focusOnLocation.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/map/MapView.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/map/MapController.kt
 */
class MapWindowToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.mapwindow_tool_name
    override val iconResId = R.drawable.ic_mapwindow
    override val categories: List<String> = emptyList()
    override fun createTool(...) = MapWindowTool(...)
}
```

**`MapWindowTool` sketch:**

```kotlin
override val window: ToolComponent.MapWindow by mapWindow(
    showMapOnActivation = true,
    mapEndBarButtons = { MapWindowEndBarButtons(navigationController) },
    navBarContent = { MapWindowNavBar(navigationController) },
    initialize = { navigationController.interactionMode = MapController.InteractionMode.LookAt },
)
```

**Resources:**
- Strings: `mapwindow_tool_name`, `mapwindow_mode_lookat`, `mapwindow_mode_fpv`, `mapwindow_zoom_in`, `mapwindow_zoom_out` (+ uk)

**Verification:** Open from catalog → embedded map appears with custom end-bar zoom buttons → mode selector switches LookAt/FPV.

---

### 5.7 Status (`status/`)

**Purpose:** Minimal `ToolComponent.Status` with coordinates and azimuth display flags.

**SDK APIs demonstrated:**
- `ToolComponent.Status`, `statusComponent { }`, `requiredStatusComponent { }` — `c4ds-sdk/.../sdk/tool/ToolComponent.kt`
- `Status.shouldShowCoordinates`, `Status.shouldShowAzimuth` — `ToolComponent.kt`
- `CommonMapInteractor.selectedPosition` — `CommonMapInteractor.kt`

**Files to create:**

```
status/
├── StatusToolDescriptor.kt
├── StatusTool.kt               # status = statusComponent(shouldShowCoordinates = true, shouldShowAzimuth = true)
└── ui/StatusBar.kt             # custom status content (position summary + azimuth)
```

**`StatusToolDescriptor`:**

```kotlin
/**
 * Demonstrates ToolComponent.Status with coordinate and azimuth display.
 *
 * SDK APIs: ToolComponent.Status, statusComponent, Status.shouldShowCoordinates,
 *           Status.shouldShowAzimuth, CommonMapInteractor.selectedPosition.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonMapInteractor.kt
 */
class StatusToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.status_tool_name
    override val iconResId = R.drawable.ic_status
    override val categories: List<String> = emptyList()
    override fun createTool(...) = StatusTool(...)
}
```

**Resources:**
- Strings: `status_tool_name`, `status_coords_label`, `status_azimuth_label` (+ uk)

**Verification:** Open from catalog → status bar appears at bottom of map → shows cursor coordinates → shows azimuth when available.

---

### 5.8 Expandable Status (`expandablestatus/`)

**Purpose:** Demonstrates the expandable variant of `ToolComponent.Status`, including `isExpanded` toggle and `shouldShowAbove` flag.

**SDK APIs demonstrated:**
- `ToolComponent.ExpandableStatus`, `expandableStatusComponent { }` — `c4ds-sdk/.../sdk/tool/ToolComponent.kt`
- `ExpandableStatus.isExpanded`, `ExpandableStatus.shouldShowAbove` — `ToolComponent.kt`
- Programmatic expand/collapse from a custom `EndBar` button

**Files to create:**

```
expandablestatus/
├── ExpandableStatusToolDescriptor.kt
├── ExpandableStatusTool.kt      # expandableStatus + endBar (expand/collapse button)
└── ui/
    ├── ExpandableStatusContent.kt  # expanded: detail panel; collapsed: summary line
    └── ExpandableEndBar.kt
```

**`ExpandableStatusToolDescriptor`:**

```kotlin
/**
 * Demonstrates ToolComponent.ExpandableStatus with programmatic expand/collapse.
 *
 * SDK APIs: ToolComponent.ExpandableStatus, expandableStatusComponent,
 *           ExpandableStatus.isExpanded, ExpandableStatus.shouldShowAbove,
 *           AbstractTool.endBar.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolEndBar.kt
 */
class ExpandableStatusToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.expandable_status_tool_name
    override val iconResId = R.drawable.ic_expandable_status
    override val categories: List<String> = emptyList()
    override fun createTool(...) = ExpandableStatusTool(...)
}
```

**`ExpandableStatusTool` sketch:**

```kotlin
private val _expandableStatus by expandableStatusComponent(isExpanded = false, shouldShowAbove = false) { ... }

override val endBar: ToolEndBar by endBar {
    EndBarToggleButton(
        icon = painterResource(R.drawable.ic_expandable_status),
        contentDescription = ...,
        isChecked = _expandableStatus.isExpanded,
        onCheckedChange = { _expandableStatus.isExpanded = it },
    )
}
```

**Resources:**
- Strings: `expandable_status_tool_name`, `expandable_status_collapse`, `expandable_status_expand`, `expandable_status_show_above` (+ uk)

**Verification:** Open from catalog → collapsed status bar visible → end bar toggle expands it → `shouldShowAbove` toggled from the expanded panel shows panel above status bar.

---

### 5.9 Underlay (`underlay/`)

**Purpose:** Demonstrates `ToolComponent.Underlay` — a composable rendered behind the map.

**SDK APIs demonstrated:**
- `ToolComponent.Underlay`, `component<ToolComponent.Underlay> { }` — `c4ds-sdk/.../sdk/tool/ToolComponent.kt`
- Simple full-screen Compose content behind the map layer

**Files to create:**

```
underlay/
├── UnderlayToolDescriptor.kt
├── UnderlayTool.kt             # underlay by requiredComponent { UnderlayContent() }
└── ui/UnderlayContent.kt       # semi-transparent background with grid/watermark
```

**`UnderlayToolDescriptor`:**

```kotlin
/**
 * Demonstrates ToolComponent.Underlay — composable rendered behind the map.
 *
 * SDK APIs: ToolComponent.Underlay, requiredComponent.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractTool.kt
 */
class UnderlayToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.underlay_tool_name
    override val iconResId = R.drawable.ic_underlay
    override val categories: List<String> = emptyList()
    override fun createTool(...) = UnderlayTool(...)
}
```

**Resources:**
- Strings: `underlay_tool_name`, `underlay_label` (+ uk)

**Verification:** Open from catalog → semi-transparent underlay visible behind map → map is still interactive on top of it.

---

### 5.10 End Bar (`endbar/`)

**Purpose:** Demonstrates the full end bar API — action button, toggle button, menu button with items and slider.

**SDK APIs demonstrated:**
- `AbstractTool.endBar { }`, `ToolEndBar` — `c4ds-sdk/.../sdk/tool/ToolEndBar.kt`
- `EndBarActionButton`, `EndBarToggleButton` — `c4ds-sdk-core/ui/.../ui/component/bar/endbar/EndBar.kt`
- `EndBarMenuButton` with `EndBarMenuScope.Checkable`, `EndBarMenuScope.Slider` — `EndBar.kt`
- `painterResource(R.drawable.*)` on EndBar icons (plugin-sourced painters) — per `FallbackResources` isolation

**Files to create:**

```
endbar/
├── EndBarSampleToolDescriptor.kt
├── EndBarSampleTool.kt         # endBar { action + toggle + menu }; window shows current state
└── ui/EndBarSampleWindow.kt    # state display: which button is toggled, slider value
```

**`EndBarSampleToolDescriptor`:**

```kotlin
/**
 * Demonstrates the full EndBar API.
 *
 * SDK APIs: AbstractTool.endBar, EndBarActionButton, EndBarToggleButton,
 *           EndBarMenuButton, EndBarMenuScope.Checkable, EndBarMenuScope.Slider,
 *           plugin painterResource on EndBar icons (FallbackResources).
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolEndBar.kt
 *   c4ds-sdk-core/ui/src/main/kotlin/vision/combat/c4/ds/sdk/ui/component/bar/endbar/EndBar.kt
 */
class EndBarSampleToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.endbar_tool_name
    override val iconResId = R.drawable.ic_end_bar
    override val categories: List<String> = emptyList()
    override fun createTool(...) = EndBarSampleTool(...)
}
```

**`EndBarSampleTool` sketch:**

```kotlin
private var toggleState by mutableStateOf(false)
private var sliderValue by mutableStateOf(0.5f)

override val endBar: ToolEndBar by endBar {
    EndBarActionButton(painterResource(R.drawable.ic_end_bar), ...) { /* action */ }
    EndBarToggleButton(painterResource(R.drawable.ic_end_bar), ..., toggleState) { toggleState = it }
    EndBarMenuButton(icon = painterResource(R.drawable.ic_end_bar), ...) {
        Checkable("Option A", isChecked = toggleState, onClick = { toggleState = !toggleState })
        Slider("Slider", sliderValue, { sliderValue = it }, 0f..1f)
    }
}
override val window: ToolComponent.Window by requiredComponent {
    EndBarSampleWindow(::toggleState, ::sliderValue)
}
```

**Resources:**
- Strings: `endbar_tool_name`, `endbar_action_cd`, `endbar_toggle_cd`, `endbar_menu_option_a`, `endbar_slider_label`, `endbar_current_toggle`, `endbar_current_slider` (+ uk)

**Verification:** Open from catalog → end bar shows three buttons → action fires (toast) → toggle syncs with window display → menu opens with checkable + slider → slider value reflected in window.

---

### 5.11 Model (`model/`)

**Purpose:** Demonstrates `CommonModelInteractor` for observing, selecting, creating, and deleting BCM models.

**SDK APIs demonstrated:**
- `CommonModelInteractor.selectedModel`, `.userModel`, `.getAllModels()`, `.getModel()` — `c4ds-sdk-core/domain/.../interactor/CommonModelInteractor.kt`
- `CommonModelInteractor.selectModel(id)`, `.unselectModel()`, `.createModel(...)`, `.deleteModel(...)` — `CommonModelInteractor.kt`
- `CommonModelInteractor.isReadOnly` — `CommonModelInteractor.kt`
- `BattlespaceConceptModel`, `ModelId`, `GeoPoint` — transitive `c4model` types

**Files to create:**

```
model/
├── ModelToolDescriptor.kt
├── ModelTool.kt
├── ModelViewModel.kt           # allModels: StateFlow (from getAllModels via modelUpdatedEvent),
│                               # selectedModel, isReadOnly
└── ui/ModelWindow.kt           # model list, select/unselect, create, delete buttons
```

**`ModelToolDescriptor`:**

```kotlin
/**
 * Demonstrates CommonModelInteractor: observe, select, create, delete BCM models.
 *
 * SDK APIs: CommonModelInteractor (getAllModels, selectedModel, userModel,
 *           selectModel, unselectModel, createModel, deleteModel, isReadOnly),
 *           BattlespaceConceptModel, ModelId.
 *
 * SDK files:
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonModelInteractor.kt
 */
class ModelToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.model_tool_name
    override val iconResId = R.drawable.ic_model
    override val categories: List<String> = emptyList()
    override fun createTool(...) = ModelTool(...)
}
```

**Resources:**
- Strings: `model_tool_name`, `model_section_selected`, `model_section_user`, `model_section_all`, `model_no_models`, `model_select`, `model_unselect`, `model_create`, `model_delete`, `model_read_only_warning` (+ uk)

**Verification:** Open from catalog → current models listed → tap model to select → create a new marker-type model → delete it → `isReadOnly = true` case disables create/delete buttons.

---

### 5.12 Service + autoStart (`service/`)

**Purpose:** Demonstrates `AbstractToolService` lifecycle (session-scoped background work), `ToolNotificationManager`, and `autoStart = true` on the descriptor.

**SDK APIs demonstrated:**
- `ToolDescriptor.createService()` → `AbstractToolService` — `c4ds-sdk/.../sdk/tool/ToolDescriptor.kt`, `AbstractToolService.kt`
- `ToolDescriptor.autoStart` — `ToolDescriptor.kt`
- `AbstractToolService.context`, coroutine scope, `onStart`/`onStop` hooks (check actual API in `AbstractToolService.kt`)
- `ToolNotificationManager` (extends `AbstractNotificationManager`) — `c4ds-sdk-core/ui/.../ui/manager/ToolNotificationManager.kt` (via `c4ds-sdk-core/platform`)
- `ToolComponent.Window` companion window showing service state

**Files to create:**

```
service/
├── ServiceToolDescriptor.kt    # autoStart = true; createService() returns ServiceSampleService
├── ServiceTool.kt              # window showing service status
├── ServiceSampleService.kt     # AbstractToolService; periodic log / counter update
└── ui/ServiceWindow.kt         # displays service running state + event count
```

**`ServiceToolDescriptor`:**

```kotlin
/**
 * Demonstrates AbstractToolService + autoStart.
 *
 * SDK APIs: ToolDescriptor.createService, ToolDescriptor.autoStart = true,
 *           AbstractToolService (onStart/onStop, coroutine scope),
 *           ToolNotificationManager.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolDescriptor.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractToolService.kt  [verify path]
 *   c4ds-sdk-core/ui/src/main/kotlin/vision/combat/c4/ds/sdk/ui/manager/ToolNotificationManager.kt
 */
class ServiceToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.service_tool_name
    override val iconResId = R.drawable.ic_service
    override val categories: List<String> = emptyList()
    override val autoStart: Boolean = true
    override fun createTool(...) = ServiceTool(...)
    override fun createService(...) = ServiceSampleService(toolContext, di)
}
```

> **Note for implementer:** Inspect `c4ds-sdk/.../sdk/tool/AbstractToolService.kt` for the exact constructor signature, available lifecycle hooks (`onStart`, `onStop`, `onDestroy`), and how to inject the service's coroutine scope. The navigation first-party tool (`c4ds/c4ds-tool/navigation/`) has a `NavigationalToolService` that is a good implementation reference.

**Resources:**
- Strings: `service_tool_name`, `service_running`, `service_stopped`, `service_events`, `service_last_event` (+ uk)

**Verification:** Install APK → service auto-starts without opening the hub → open hub → open ServiceTool window → event counter increments → close/reopen: counter continues from where it was.

---

### 5.13 Resources — Config & Font/Raw (`resources/config/`)

**Purpose:** Consolidates the current `config` and `fontraw` isolation samples into one `resources` sample demonstrating locale + night + rotation config reactivity, plugin font, and plugin raw resource.

**SDK APIs demonstrated:**
- Plugin `stringResource(R.string.*)` with `values/`, `values-uk/`, `values-night/`, `values-night-uk/` qualifiers
- `LocalConfiguration` — recomposition on config change (locale, night mode)
- Plugin `fontResource(R.font.*)`, `FontFamily(Font(R.font.sample_font))`
- Plugin `context.resources.openRawResource(R.raw.sample_note)` (imperative read in `ViewModel`)
- `drawable(-night)` qualified vector drawable in `painterResource`

**Files to create:**

```
resources/config/
├── ConfigToolDescriptor.kt
├── ConfigTool.kt
├── ConfigViewModel.kt          # reads R.raw.sample_note via toolContext.resources.openRawResource
└── ui/ConfigWindow.kt          # shows: day/night string, locale string, font demo, raw content
```

**`ConfigToolDescriptor`:**

```kotlin
/**
 * Demonstrates config-qualified resources: locale, night mode, font, raw.
 *
 * SDK APIs: Plugin stringResource with values-uk/values-night/values-night-uk,
 *           LocalConfiguration (recomposition on config change),
 *           painterResource with drawable-night qualifier,
 *           FontFamily(Font(R.font.*)), context.resources.openRawResource.
 *
 * SDK files: (resource API is Android SDK; plugin context isolation is c4ds-specific)
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolContext.kt
 */
class ConfigToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.config_tool_name
    override val iconResId = R.drawable.ic_resources
    override val categories: List<String> = emptyList()
    override fun createTool(...) = ConfigTool(...)
}
```

**Resources needed:**
- `values/strings.xml`: `config_tool_name`, `config_mode` = `"DAY mode (en)"`, `config_font_label`, `config_raw_label`
- `values-uk/strings.xml`: Ukrainian equivalents, `config_mode` = `"ДЕННИЙ режим (uk)"`
- `values-night/strings.xml`: `config_mode` = `"NIGHT mode active"`
- `values-night-uk/strings.xml`: `config_mode` = `"НІЧНИЙ режим (uk)"`
- `drawable/ic_resources.xml`: sun icon (day)
- `drawable-night/ic_resources.xml`: crescent moon icon (night)
- `font/sample_font.ttf`: copy from current `:window`
- `raw/sample_note.txt`: copy from current `:window`

**Verification:** Toggle system dark mode → `config_mode` string and icon switch live. Switch locale → Ukrainian strings appear. Font displays with `sample_font.ttf`. Raw file content shown in window.

---

### 5.14 Resources — Material Composition Fallback (`resources/material/`)

**Purpose:** Verifies that Compose Material (M2) widgets compiled into the plugin (`Scaffold`, `SnackbarHost`, `AlertDialog`, `DropdownMenu`, `Slider`) work without `Resources$NotFoundException` thanks to `CompositionFallbackContext`.

**SDK APIs demonstrated:**
- M2 `Scaffold`, `SnackbarHost`, `rememberScaffoldState` (plugin-compiled)
- M2 `AlertDialog`, `DropdownMenu`, `Slider` (plugin-compiled)
- `CompositionFallbackContext` behavior (transparent to plugin authors — just note that it exists)

**Files to create:**

```
resources/material/
├── MaterialToolDescriptor.kt
├── MaterialTool.kt
└── ui/MaterialWindow.kt        # Scaffold + SnackbarHost + buttons for AlertDialog, DropdownMenu, Slider
```

**`MaterialToolDescriptor`:**

```kotlin
/**
 * Verifies that plugin-compiled Compose Material widgets work inside the host
 * via CompositionFallbackContext (no Resources$NotFoundException).
 *
 * SDK APIs: (internal) CompositionFallbackContext — transparent to plugin authors.
 *           Plugin-compiled: M2 Scaffold, SnackbarHost, AlertDialog, DropdownMenu, Slider.
 *
 * SDK files (internal, not callable from plugins):
 *   c4ds-sdk-core/host/src/main/kotlin/vision/combat/c4/ds/sdk/host/CompositionFallbackContext.kt
 */
class MaterialToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.material_tool_name
    override val iconResId = R.drawable.ic_material
    override val categories: List<String> = emptyList()
    override fun createTool(...) = MaterialTool(...)
}
```

**Resources:**
- Strings: `material_tool_name`, `material_show_snackbar`, `material_show_dialog`, `material_dialog_title`, `material_dialog_body`, `material_dropdown_label`, `material_slider_label`, `material_explainer` (+ uk)

**Verification:** Open from catalog → window renders without crash → Snackbar shows and auto-dismisses → AlertDialog opens and closes → DropdownMenu items selectable → Slider draggable.

---

### 5.15 Resources — R.string Collision (`resources/collision/`)

**Purpose:** Demonstrates that plugin `R.string` values take priority over identically-named host strings.

**SDK APIs demonstrated:**
- Plugin-first resource resolution via `FallbackResources` (internal — transparent to plugin authors)
- Plugin `R.string.settings` = `"PLUGIN settings (isolation wins)"` vs host `R.string.settings`

**Files to create:**

```
resources/collision/
├── CollisionToolDescriptor.kt
├── CollisionTool.kt
└── ui/CollisionWindow.kt       # shows R.string.settings value; explains expected vs actual
```

**`CollisionToolDescriptor`:**

```kotlin
/**
 * Demonstrates plugin-first R.string resolution when plugin and host share a string name.
 *
 * SDK APIs: (internal) FallbackResources — transparent to plugin authors.
 *           Plugin declares R.string.settings = "PLUGIN settings (isolation wins)".
 *
 * SDK files (internal):
 *   c4ds-sdk-core/host/src/main/kotlin/vision/combat/c4/ds/sdk/host/FallbackResources.kt
 */
class CollisionToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.collision_tool_name
    override val iconResId = R.drawable.ic_collision
    override val categories: List<String> = emptyList()
    override fun createTool(...) = CollisionTool(...)
}
```

**Resources:**
- `values/strings.xml`: `collision_tool_name`, `settings` = `"PLUGIN settings (isolation wins)"`, `collision_explainer` (+ uk)

**Verification:** Open from catalog → `settings` string shows plugin value, not host value.

---

## 6. Isolation APK specs (`:isolation`)

### 6.1 Native Tool (`nativelib/`)

> **Naming note:** the subpackage is `nativelib`, not `native`. `native` is a reserved Java keyword and is invalid as an Android package/namespace segment (it breaks AAPT/`R` generation and Kotlin-to-Java interop).

**Purpose:** Demonstrates cross-APK multi-plugin coexistence, per-APK classloader isolation, native `.so` loading from a separate APK's `nativeLibraryDir`, and `toolContext.assets` returning the plugin APK's `AssetManager`.

**SDK APIs demonstrated:**
- `ToolDescriptor`, `AbstractTool`, `ToolComponent.Window` — same as any other tool
- `System.loadLibrary("isolation_jni")` called imperatively in `NativeTool.onComponentShown`
- `toolContext.assets.open("isolation/sample.txt")` — plugin AssetManager isolation
- Cross-APK activation from `:gallery` hub via `ToolManager.resolveToolId(fqcn)`

**Files to create:**

```
isolation/nativelib/
├── NativeToolDescriptor.kt
├── NativeTool.kt               # onComponentShown → smokeTestAssets() + smokeTestNativeLib()
├── NativeToolWindow.kt         # shows JNI result + asset read result
└── IsolationNative.kt          # object with tryLoad() + nativeVersion(): String
```

**`NativeToolDescriptor`:**

```kotlin
/**
 * Demonstrates cross-APK isolation: separate classloader, native .so from plugin nativeLibraryDir,
 * plugin AssetManager via toolContext.assets.
 *
 * This tool lives in :isolation APK (package vision.combat.c4.ds.sample.isolation.nativelib).
 * It is launched cross-APK from :gallery's CatalogTool via ToolManager.resolveToolId(fqcn).
 *
 * SDK APIs: ToolContext.assets (plugin AssetManager), System.loadLibrary from plugin nativeLibraryDir,
 *           ToolComponent.Window.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolContext.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractTool.kt
 */
class NativeToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId = R.string.native_tool_name
    override val iconResId = R.drawable.ic_isolation
    override val categories: List<String> = emptyList()
    override fun createTool(...) = NativeTool(...)
}
```

**`NativeTool` pattern:**

Carry over the smoke-test pattern from the current `OverlayTool.kt`:

```kotlin
override fun onComponentShown(component: ToolComponent) {
    super.onComponentShown(component)
    if (component is ToolComponent.Window) {
        smokeTestAssets()
        smokeTestNativeLib()
    }
}

private fun smokeTestAssets() {
    runCatching {
        val content = toolContext.assets.open("isolation/sample.txt").use { it.readBytes() }.decodeToString()
        Log.i(TAG, "[ASSET SMOKE] Read 'isolation/sample.txt' from :isolation plugin. Content: $content")
        // update ViewModel state to show in window
    }.onFailure { e ->
        Log.e(TAG, "[ASSET SMOKE] FAILED — check ToolContext.getAssets() isolation", e)
    }
}

private fun smokeTestNativeLib() {
    IsolationNative.tryLoad()
        .onSuccess {
            val version = IsolationNative.nativeVersion()
            Log.i(TAG, "[JNI SMOKE] nativeVersion() = '$version' — isolation OK")
        }
        .onFailure { e ->
            Log.e(TAG, "[JNI SMOKE] FAILED to load libisolation_jni.so", e)
        }
}
```

**`IsolationNative`:**

```kotlin
object IsolationNative {
    private var loaded = false
    fun tryLoad(): Result<Unit> = runCatching { if (!loaded) { System.loadLibrary("isolation_jni"); loaded = true } }
    external fun nativeVersion(): String
}
```

**CMake / JNI:**

Rename `overlay_jni.cpp` → `isolation_jni.cpp`. Rename the CMake target to `isolation_jni`. The `nativeVersion()` function returns `"isolation-jni/1.0"`.

**`assets/isolation/sample.txt`:**

Content: multi-line UTF-8 text explaining that this file is read from the `:isolation` plugin APK's AssetManager (not the host's).

**Resources:**
- `values/strings.xml`: `app_name`, `native_tool_name`, `native_jni_label`, `native_asset_label`, `native_not_loaded`, `native_cross_apk_explainer` (+ uk)

**Verification:**
1. Install both `:gallery` and `:isolation` APKs.
2. Open hub → scroll to Resources & Isolation → "Native / Cross-APK" card → **Launch**.
3. Window opens → JNI result row shows `"isolation-jni/1.0"`.
4. Asset read row shows file content prefix.
5. Logcat shows `[ASSET SMOKE] Read 'isolation/sample.txt' from :isolation plugin` — isolation OK.
6. Logcat shows `[JNI SMOKE] nativeVersion() = 'isolation-jni/1.0'` — .so loaded from plugin OK.

---

## 7. Conventions

### 7.1 File and class naming

| Item | Pattern | Example |
|---|---|---|
| Descriptor | `<Area>ToolDescriptor` | `WindowSimpleToolDescriptor` |
| Tool | `<Area>Tool` | `WindowSimpleTool` |
| Window composable | `<Area>Window` | `WindowSimpleWindow` |
| Overlay composable | `<Area>Overlay` | `OverlaySampleOverlay` |
| Status composable | `<Area>StatusBar` | `MapStatusBar` |
| ViewModel | `<Area>ViewModel` | `ModelViewModel` |
| Subpackage | lowercase, no hyphens | `expandablestatus`, `mapwindow`, `endbar` |

### 7.2 `ToolDescriptor` template

Every non-hub descriptor follows this template:

```kotlin
/**
 * <One-line: what this sample demonstrates.>
 *
 * SDK APIs: <comma-separated list of interfaces/classes/composables demonstrated>.
 *
 * SDK files:
 *   <path/to/PrimaryApiFile.kt>
 *   <path/to/SecondaryApiFile.kt>
 */
class <Area>ToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.<area>_tool_name
    override val iconResId: Int = R.drawable.ic_<area>
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return <Area>Tool(toolContext, this, di, params)
    }
}
```

### 7.3 i18n rules

- Every user-visible string in `gallery/src/main/res/values/strings.xml`.
- Ukrainian equivalents in `gallery/src/main/res/values-uk/strings.xml` — no string may be missing from `values-uk`.
- Night-mode overrides only where the tool explicitly demonstrates config reactivity (`config` sample): `values-night/` and `values-night-uk/`.
- No hardcoded English text in any `.kt` file.

### 7.4 `combat_tools.xml` and manifest

- `combat_tools.xml` must list every `ToolDescriptor` in the APK — one `<tool-descriptor name="..."/>` per descriptor.
- The manifest `<application>` must carry exactly one `<meta-data android:name="vision.combat.c4.ds.sdk.DECLARED_TOOLS" android:resource="@xml/combat_tools"/>`.

### 7.5 Typos and inconsistencies to fix

| Current | Fix |
|---|---|
| `navigation/data/respository/` | rename to `repository/` |
| `combat-ds-sdk-runtine` in `libs.versions.toml` | rename to `combat-ds-sdk-runtime` |
| All occurrences of `runtine` in `build.gradle.kts` files | rename to `runtime` |
| Split package `vision.combat.c4.ds.example.tool.*` vs `vision.combat.c4.ds.tool.sample.*` | unified `vision.combat.c4.ds.sample.*` |

### 7.6 Build config

- `minSdk = 26` for both APKs (aligned with current samples).
- `compileSdk = targetSdk = 37` for both APKs.
- Release builds sign with `debug.keystore` (already at repo root) — acceptable for samples.
- `:isolation` requires `packaging { jniLibs { useLegacyPackaging = true } }` and `ndk { abiFilters += listOf("arm64-v8a", "x86_64") }`.
- Both APKs use `coreLibraryDesugaring(libs.android.tools.desugar)`.

---

## 8. Migration map — current → target

### `:overlay` APK → `:isolation` APK

| Current | Target |
|---|---|
| `OverlayToolDescriptor` | `NativeToolDescriptor` in `:isolation` |
| `OverlayTool` (JNI + assets) | `NativeTool` in `:isolation` |
| `OverlayToolOverlay` (map overlay) | **New** `OverlaySampleTool` in `:gallery` (overlay; no JNI) |
| `OverlayNative` | `IsolationNative` in `:isolation` |
| `overlay_jni.cpp` | `isolation_jni.cpp` |
| `assets/overlay/sample.txt` | `assets/isolation/sample.txt` |
| Map + model interactor logic in `OverlayToolViewModel` | Move to new `OverlaySampleViewModel` in `:gallery/overlay/` |

### `:window` APK → `:gallery` APK

| Current tool | Target | Action |
|---|---|---|
| `SimpleTool` / `SimpleToolDescriptor` | `window/simple/WindowSimpleTool` | Port + rename; fix hardcoded strings |
| `NavigationTool` / `NavigationToolDescriptor` | `window/navigation/WindowNavTool` | Port + rename; fix hardcoded strings; fix `respository` typo |
| `MaterialTool` | `resources/material/MaterialTool` | Port into resources subpackage |
| `CollisionTool` | `resources/collision/CollisionTool` | Port into resources subpackage |
| `ConfigTool` + `FontRawTool` | `resources/config/ConfigTool` | Merge into one sample; add raw file read |
| `EndBarTool` | `endbar/EndBarSampleTool` | Port + add `EndBarMenuButton` with items |
| — (no current) | `dialog/DialogTool` | New |
| — (no current) | `map/MapTool` | New (from `AbstractMapTool`) |
| — (no current) | `mapwindow/MapWindowTool` | New |
| — (no current) | `status/StatusTool` | New |
| — (no current) | `expandablestatus/ExpandableStatusTool` | New |
| — (no current) | `underlay/UnderlayTool` | New |
| — (no current) | `model/ModelTool` | New |
| — (no current) | `service/ServiceTool` | New |
| — (no current) | `catalog/CatalogTool` | New (hub) |

### Delete

The entire existing `:overlay` and `:window` module directories after porting.

---

## 9. README rewrite outline

Replace the current `README.md` at the repo root with the following structure:

```markdown
# c4ds Tool Samples

One-paragraph description: this repo contains two sample plugin APKs that cover the full
public SDK surface for external ComBat 4 tool authors.

## Quick start

Prerequisites (host app version, Nexus credentials, SDK version, Kotlin/Compose lockstep).

### Nexus credentials

`c4ds_sdk_username` and `c4ds_sdk_password` in `~/.gradle/gradle.properties`.

### Build and install

\`\`\`bash
./gradlew :gallery:assembleRelease :isolation:assembleRelease
adb install -r gallery/build/outputs/apk/release/gallery-release.apk
adb install -r isolation/build/outputs/apk/release/isolation-release.apk
\`\`\`

No Activity — install both APKs then launch ComBat 4.

## APK contents

### :gallery — gallery.apk

Hub entry point + all feature samples. Only the "Sample Gallery" tool appears in the
host launcher. All others are launched from inside the hub.

| Sample | Section | SDK surface |
|---|---|---|
| Window Simple | Windows | ToolComponent.Window, WindowScaffold, SDK inputs, ModelInteractor |
| Window Navigation | Windows | AppNavHost, Route, subDI, SharedPreferences |
| Dialog | Windows | ToolDialog variants, showDialog/dismissDialog |
| Overlay | Map | ToolComponent.Overlay, MapInteractor, ModelInteractor |
| Map | Map | AbstractMapTool, RenderableLayer, SelectDragCallback, Status |
| MapWindow | Map | ToolComponent.MapWindow, embedded MapView, MapController |
| Status | Status & Bars | ToolComponent.Status, shouldShowCoordinates/Azimuth |
| Expandable Status | Status & Bars | ToolComponent.ExpandableStatus, isExpanded |
| Underlay | Status & Bars | ToolComponent.Underlay |
| End Bar | Status & Bars | EndBarActionButton, EndBarToggleButton, EndBarMenuButton |
| Model | Model & Lifecycle | CommonModelInteractor CRUD |
| Service | Model & Lifecycle | AbstractToolService, autoStart, ToolNotificationManager |
| Resources / Config | Resources | Locale, night, config-qualified resources, font, raw |
| Resources / Material | Resources | Plugin-compiled M2 widgets, CompositionFallbackContext |
| Resources / Collision | Resources | Plugin-first R.string resolution |

### :isolation — isolation.apk

| Sample | Section | SDK surface |
|---|---|---|
| Native / Cross-APK | Resources & Isolation | Per-APK ClassLoader, nativeLibraryDir .so, AssetManager |

## Integration guide

Step-by-step for external tool authors:
1. Create an Android Application module (no Activity).
2. Add `compileOnly(libs.combat.ds.sdk)` + `runtimeOnly(libs.combat.ds.sdk.runtime)`.
3. Subclass `ToolDescriptor` + `AbstractTool`.
4. Create `res/xml/combat_tools.xml` listing your descriptor FQCN.
5. Add `DECLARED_TOOLS` meta-data to `AndroidManifest.xml`.

## Plugin isolation cases

### Case (a): M2 composition fallback (resources/material sample)
...

### Case (b): R.string collision (resources/collision sample)
...

### Case (c): Config reactivity (resources/config sample)
...

### Case (d): Pinned state survives versionCode bump (manual procedure)

This case is not a dedicated tool. It requires two installs.

Procedure:
1. Build and install `:gallery` with `versionCode = 1`. Open a sample tool and note its state.
2. Bump `versionCode` to 2 in `:gallery/build.gradle.kts`.
3. Rebuild and reinstall (package manager replace, same `applicationId`).
4. Verify that `ToolManager` correctly resolves the updated descriptors and that any
   pinned/persisted state the tool stored in `SharedPreferences` survived the update.

### Case (e): Font + raw resource (resources/config sample)
...

### Case (g): EndBar Painter API (endbar sample)
...

### Case (h): Cross-APK native .so + assets (isolation sample)
...
```

---

## 10. Build and verification steps

Run from the repo root after all code is written.

### Build

```bash
# Requires c4ds_sdk_username and c4ds_sdk_password in ~/.gradle/gradle.properties
./gradlew :gallery:assembleRelease
./gradlew :isolation:assembleRelease
```

Both must succeed without warnings about missing string resources.

### Install

```bash
adb install -r gallery/build/outputs/apk/release/gallery-release.apk
adb install -r isolation/build/outputs/apk/release/isolation-release.apk
```

### Launcher check

Open the host app launcher list. Exactly **one** tool from the gallery package appears: `"Sample Gallery"` (or the localized equivalent). No other sample descriptors appear in the launcher.

### Hub / catalog check

Open the Sample Gallery tool. Verify:
- All 5 sections are present with correct headings.
- Every sample has a card with a name, description, **Launch** and **Details** button.
- The `Native / Cross-APK` card in the `:isolation` section is enabled (not grayed out) after the isolation APK is installed. It is grayed out when only the gallery APK is installed.

### Per-sample smoke tests

| Sample | Smoke test |
|---|---|
| Window Simple | Form inputs accept values; model section shows current model; Unselect fires toast |
| Window Navigation | Navigate to Settings; toggle persists after close/reopen; "Launch Window Simple" opens it on top |
| Dialog | All four dialog types open/close correctly |
| Overlay | Overlay appears on map; cursor coords update with map movement |
| Map | Tap terrain → Placemark appears; status bar updates |
| MapWindow | Embedded map renders; zoom buttons work; mode selector switches |
| Status | Status bar visible; coordinates and azimuth shown |
| Expandable Status | Collapse/expand toggles; shouldShowAbove moves panel |
| Underlay | Semi-transparent layer visible behind map |
| End Bar | Action, toggle, menu buttons all function; window reflects state |
| Model | Model list populated; select/unselect/create/delete work |
| Service | After install (no launch), service is already running (autoStart); window shows counter |
| Resources / Config | Toggle dark mode → string + icon update live; switch locale → Ukrainian strings |
| Resources / Material | No crash; Snackbar, AlertDialog, DropdownMenu, Slider all work |
| Resources / Collision | `settings` string shows plugin value |
| Native / Cross-APK | Logcat shows asset + JNI smoke lines; window displays results |

### i18n smoke test

Switch system locale to Ukrainian. Open each sample from the hub. Verify:
- All visible strings are in Ukrainian — no English strings visible except intentional code-level labels.

### Locale/night reactivity

1. Open `Resources / Config` from hub.
2. Toggle system dark mode → `config_mode` string switches; day/night icon switches.
3. Switch to Ukrainian locale → all strings in window switch to Ukrainian, including `config_mode`.

### Case (d) — pinned-state manual smoke test

See README §Case (d) procedure above.
