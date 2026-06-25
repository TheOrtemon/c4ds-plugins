# Samples catalog

Reference for every tool in this repository: what it demonstrates, where the source lives, and how to verify it works.

**Package root:** `vision.combat.c4.ds.sample.*`  
**Hub:** `gallery/.../catalog/CatalogTool` — the only launcher-visible tool in `:gallery`.

---

## How to navigate

| Action | Result |
|---|---|
| Open **Sample Gallery** from host Tools list | Hub window with grouped cards |
| **Launch** on a card | Activates that sample (same APK via `ToolManager.activate<T>()`; cross-APK via `resolveToolId`) |
| **Details** on a card | Shows SDK APIs and source subpackage |
| Install `:isolation` APK | Enables **Native / Cross-APK** card in the Resources & Isolation section |

Registry implementation: [`SampleCatalog.kt`](../gallery/src/main/kotlin/vision/combat/c4/ds/sample/gallery/catalog/SampleCatalog.kt)

---

## Windows

### Window Simple

| | |
|---|---|
| **Purpose** | Minimum viable window tool — form inputs and model interactor |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.window.simple.WindowSimpleToolDescriptor` |
| **Source** | `gallery/src/main/kotlin/vision/combat/c4/ds/sample/gallery/window/simple/` |

**SDK APIs:** `ToolComponent.Window`, `requiredComponent`, `WindowScaffold`, `BackNavTopAppBar`, measurement inputs (`DistanceInput`, `SpeedInput`, `AltitudeInput`, `AngleInput`, `CoordinatesInputWithSystem`), `CommonModelInteractor` (`selectedModel`, `userModel`, `unselectModel`), `diViewModel()`, `showToast`.

**Verify:** Launch from hub → window opens → inputs accept values → model section shows selected/user model → **Unselect** shows toast.

---

### Window Navigation

| | |
|---|---|
| **Purpose** | Multi-screen window with tool-scoped DI and persisted settings |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.window.navigation.WindowNavToolDescriptor` |
| **Source** | `gallery/.../window/navigation/` |

**SDK APIs:** `AppNavHost`, `Route`, navigation transitions, `BackNavigationButton`, `subDI { import(module) }`, tool-scoped `SharedPreferences`, `ToolManager.activate<WindowSimpleToolDescriptor>(FLAG_COMPONENT_ON_TOP)`.

**Verify:** Home screen → navigate to Settings → toggle **Open Simple on top** → close and reopen tool → toggle persisted → **Launch Window Simple** opens Window Simple on top.

---

### Dialog

| | |
|---|---|
| **Purpose** | All four `ToolDialog` variants |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.dialog.DialogToolDescriptor` |
| **Source** | `gallery/.../dialog/` |

**SDK APIs:** `ToolDialog.Confirmation`, `.Destructive`, `.Info`, `.Custom`, `AbstractTool.showDialog()`, `dismissDialog()`, `DialogHeader`, `ButtonsRow`.

**Verify:** Four buttons each open the correct dialog type; confirm and dismiss work for every variant.

---

## Map

### Overlay

| | |
|---|---|
| **Purpose** | Map overlay composable with cursor position and user model |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.overlay.OverlaySampleToolDescriptor` |
| **Source** | `gallery/.../overlay/` |

**SDK APIs:** `ToolComponent.Overlay`, `CommonMapInteractor.selectedPosition`, `CommonModelInteractor.userModel`, `CommonLocaleSettingsInteractor.coordinateSystemFormat`, overlay theme tokens.

**Verify:** Overlay visible on map → cursor coordinates update when panning → user model name appears when set.

---

### Map

| | |
|---|---|
| **Purpose** | Map tap handling, renderable layers, status bar integration |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.map.MapToolDescriptor` |
| **Source** | `gallery/.../map/` |

**SDK APIs:** `AbstractMapTool`, `RenderableLayer`, `SelectDragCallback`, map tap callbacks, `ToolComponent.Status`, `shouldShowCoordinates`, `shouldShowAzimuth`.

**Verify:** Tap terrain → placemark appears → status bar shows coordinates/azimuth.

---

### MapWindow

| | |
|---|---|
| **Purpose** | Embedded secondary map inside a window panel |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.mapwindow.MapWindowToolDescriptor` |
| **Source** | `gallery/.../mapwindow/` |

**SDK APIs:** `ToolComponent.MapWindow`, embedded `MapView`, `MapController`, zoom controls, map mode selector.

**Verify:** Window opens with embedded map → zoom in/out work → mode selector switches view.

---

## Status & bars

### Status

| | |
|---|---|
| **Purpose** | Basic status bar with coordinates and azimuth |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.status.StatusToolDescriptor` |
| **Source** | `gallery/.../status/` |

**SDK APIs:** `ToolComponent.Status`, `shouldShowCoordinates`, `shouldShowAzimuth`.

**Verify:** Status strip visible at bottom → coordinates and azimuth update with map interaction.

---

### Expandable Status

| | |
|---|---|
| **Purpose** | Collapsible status panel above or below the strip |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.expandablestatus.ExpandableStatusToolDescriptor` |
| **Source** | `gallery/.../expandablestatus/` |

**SDK APIs:** `ToolComponent.ExpandableStatus`, `isExpanded`, `shouldShowAbove`.

**Verify:** Toggle expand/collapse → panel moves above/below per **Show above** setting.

---

### Underlay

| | |
|---|---|
| **Purpose** | Full-screen layer under the main map |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.underlay.UnderlayToolDescriptor` |
| **Source** | `gallery/.../underlay/` |

**SDK APIs:** `ToolComponent.Underlay`.

**Verify:** Semi-transparent underlay visible behind map content.

---

### End Bar

| | |
|---|---|
| **Purpose** | Custom buttons on the map's right edge |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.endbar.EndBarSampleToolDescriptor` |
| **Source** | `gallery/.../endbar/` |

**SDK APIs:** `endBarButtons`, `EndBarActionButton`, `EndBarToggleButton`, `EndBarMenuButton`, `painterResource` in end-bar scope.

**Verify:** Action, toggle, and menu buttons appear on end bar → toggle state reflected in window → menu items work.

---

## Model & lifecycle

### Model

| | |
|---|---|
| **Purpose** | Battlespace model CRUD via domain interactor |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.model.ModelToolDescriptor` |
| **Source** | `gallery/.../model/` |

**SDK APIs:** `CommonModelInteractor` (list, select, unselect, create, delete), `isReadOnly`, `diViewModel()`.

**Verify:** Model list populated → select/unselect → create and delete operations succeed (when not read-only).

---

### Service

| | |
|---|---|
| **Purpose** | Background tool service with notification and auto-start |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.service.ServiceToolDescriptor` |
| **Source** | `gallery/.../service/` |

**SDK APIs:** `AbstractToolService`, `autoStart` on descriptor, `ToolNotificationManager`, foreground service lifecycle.

**Verify:** After installing gallery APK (without launching service tool manually), service is already running → open tool window → counter increments → notification visible.

---

## Resources & isolation (`:gallery`)

### Resources / Config

| | |
|---|---|
| **Purpose** | Locale, night mode, config-qualified resources, custom font, raw file |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.resources.config.ConfigToolDescriptor` |
| **Source** | `gallery/.../resources/config/` |

**SDK APIs:** Configuration-qualified `values`, `FontFamily(Font(R.font.sample_font))`, `openRawResource(R.raw.sample_note)`, live recomposition on locale/night change.

**Verify:** Toggle system dark mode → mode string and icon update live → switch to Ukrainian → all strings localize.

Covers isolation **case (c)** and **case (e)** — see [Plugin isolation](plugin-isolation.md).

---

### Resources / Material

| | |
|---|---|
| **Purpose** | Plugin-compiled Material 2 widgets with composition fallback |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.resources.material.MaterialToolDescriptor` |
| **Source** | `gallery/.../resources/material/` |

**SDK APIs:** Plugin-local M2 (`Snackbar`, `AlertDialog`, `DropdownMenu`, `Slider`), `CompositionFallbackContext`, `FallbackResources`.

**Verify:** Window opens without crash → all four widget demos interactive.

Covers isolation **case (a)**.

---

### Resources / Collision

| | |
|---|---|
| **Purpose** | Plugin-first `R.string` resolution when names collide with host |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.resources.collision.CollisionToolDescriptor` |
| **Source** | `gallery/.../resources/collision/` |

**SDK APIs:** Plugin `R.string.settings` vs host `R.string.settings` — plugin value wins.

**Verify:** Window shows plugin-specific settings string, not the host default.

Covers isolation **case (b)**.

---

## Native / cross-APK (`:isolation`)

### Native Tool

| | |
|---|---|
| **Purpose** | Per-APK ClassLoader, `nativeLibraryDir` `.so`, plugin `AssetManager`, cross-APK activation |
| **Descriptor** | `vision.combat.c4.ds.sample.isolation.nativelib.NativeToolDescriptor` |
| **Source** | `isolation/src/main/kotlin/vision/combat/c4/ds/sample/isolation/nativelib/` |
| **Native** | `isolation/src/main/cpp/` (CMake → `libisolation_jni.so`) |
| **Assets** | `isolation/src/main/assets/isolation/sample.txt` |

**SDK APIs:** `ToolManager.resolveToolId(fqcn)`, `ToolContext.assets`, `System.loadLibrary`, JNI from plugin `nativeLibraryDir`.

**Verify:**

1. Install both `:gallery` and `:isolation` APKs.
2. Open Sample Gallery → **Native / Cross-APK** → **Launch**.
3. Window shows asset content prefix and JNI result `isolation-jni/1.0`.
4. Logcat tag `NativeTool`:

   ```
   [ASSET SMOKE] Read 'isolation/sample.txt' from :isolation plugin ...
   [JNI SMOKE] nativeVersion() = 'isolation-jni/1.0' — .so loaded from plugin OK
   ```

Covers isolation **case (h)**. Full procedures: [Plugin isolation](plugin-isolation.md).

---

## i18n check

All user-visible strings exist in `values/` and `values-uk/`. After switching system locale to Ukrainian, open each sample from the hub and confirm no English leaks (except intentional code-level debug labels).

---

## Manual upgrade test (case d)

Not tied to a single tool — validates host behavior across APK updates:

1. Install `:gallery` with `versionCode = 1`. Open **Window Navigation**, set a preference.
2. Bump `versionCode` in [`gallery/build.gradle.kts`](../gallery/build.gradle.kts), rebuild, reinstall (same `applicationId`).
3. Confirm SharedPreferences state survived and hub still lists all samples.

See [Plugin isolation — case (d)](plugin-isolation.md#case-d-pinned-state-survives-versioncode-bump).
