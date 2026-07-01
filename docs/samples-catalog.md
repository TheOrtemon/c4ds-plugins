# Samples catalog

Reference for every tool in this repository: what it demonstrates, where the source lives, and how to verify it works.

**Package root:** `vision.combat.c4.ds.sample.*`  
**Hub:** `gallery/.../catalog/CatalogTool` — the only launcher-visible tool in `:gallery`.

---

## How to navigate

The gallery uses a **3-level** navigation hierarchy:

| Level | Screen | Description |
|---|---|---|
| 1 | **Category list** (root) | 11 category cards, each showing a title, description, and icon. Tap a card to drill into that category. |
| 2 | **Category detail** | Filtered list of samples in the chosen category. Tap a sample row to activate/deactivate it; tap the **ⓘ** icon to see details. |
| 3 | **Sample detail** | SDK APIs, source subpackage, and cross-APK install steps (where applicable). |

| Action | Result |
|---|---|
| Open **Sample Gallery** from host Tools list | Category list with 11 section cards |
| **Tap** a category card | Opens the category subscreen showing samples in that section |
| **Tap** an inactive sample row | Activates that sample (`ToolManager.activate<T>(FLAG_COMPONENT_ON_TOP)`); name highlighted in accent color |
| **Tap** an active sample row (accent color) | Deactivates that sample via `ToolManager.deactivate`; name returns to normal color |
| **Deactivate all** button in the app bar (root) | Deactivates every active gallery tool except the hub itself; shows a confirmation toast |
| **Info icon** (ⓘ) on a sample row | Navigates to the sample detail screen |
| Install `:isolation` APK | Enables **Native / Cross-APK** row in the Resources & Isolation section |

Registry implementation: [`CatalogEntry.kt`](../gallery/src/main/kotlin/vision/combat/c4/ds/sample/gallery/catalog/ui/CatalogEntry.kt)

---

## Section 1 — Map View

Samples: MAP, RENDERABLE, MAP_INTERACTOR

### Map (AbstractMapTool)

| | |
|---|---|
| **Purpose** | Map tap handling, renderable layers, status bar integration, info window |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.map.MapToolDescriptor` |
| **Source** | `gallery/.../map/` |

**SDK APIs:** `AbstractMapTool`, `RenderableLayer`, `SelectDragCallback`, map tap callbacks, `ToolComponent.Status`, `ToolComponent.Window`, `shouldShowCoordinates`, `shouldShowAzimuth`.

**Verify:** Tap terrain → placemark appears → status bar shows coordinates/azimuth → open info window for interaction overview.

---

### Renderables

| | |
|---|---|
| **Purpose** | Add/remove symbols, polylines, and polygons on the map with color/size customization |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.renderable.RenderableSampleToolDescriptor` |
| **Source** | `gallery/.../renderable/` |

**SDK APIs:** `RenderableLayer`, `addRenderable`, `removeAllRenderables`, `ToolComponent.Window`, `requiredComponent`, `WindowScaffold`.

**Verify:** Window opens → add each renderable type → adjust color/size → renderables appear on map → clearing removes all.

---

### Map Interactor (CommonMapInteractor)

| | |
|---|---|
| **Purpose** | Live `CommonMapInteractor` readout and controls for camera, display mode, reticle, cursor, focus, and magnetic corrections |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.mapinteractor.MapInteractorToolDescriptor` |
| **Source** | `gallery/.../mapinteractor/` |

**SDK APIs:** `CommonMapInteractor`, `mapNavigatorEvent`, `camera`, `lookAt`, `selectedPosition`, `isLookAtAboveHorizon`, `mapDisplayMode`, `updateMapDisplayMode`, `arDistanceLimit`, `setArDistanceLimit`, `isReticleVisible`, `setReticleVisible`, `isCursorPinned`, `pinCursor`, `unpinCursor`, `isMapVisible`, `setMapVisible`, `focusOnLocation`, `focusOnSector`, `getDeclination`, `getConvergence`, `getAngleCorrection`. Also `CommonModelInteractor.userModel` for focus-on-user.

**Verify:** Launch → window shows live camera/LookAt readout → switch display mode → toggle reticle/map visibility → pin/unpin cursor → **Focus on cursor** moves map to cursor position → **Focus on user** moves map to user location (no-op when no GPS fix) → declination/convergence values update at LookAt.

---

## Section 2 — Map Overlays

Samples: OVERLAY, STATUS, EXPANDABLE_STATUS, ENDBAR

### Overlay

| | |
|---|---|
| **Purpose** | Map overlay composable with cursor position and user model |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.overlay.OverlaySampleToolDescriptor` |
| **Source** | `gallery/.../overlay/` |

**SDK APIs:** `ToolComponent.Overlay`, `CommonMapInteractor.selectedPosition`, `CommonModelInteractor.userModel`, `CommonLocaleSettingsInteractor.coordinateSystemFormat`.

**Verify:** Overlay visible on map → cursor coordinates update when panning → user model name appears when set.

---

### Status

| | |
|---|---|
| **Purpose** | Status bar with host coordinate/azimuth chrome flags |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.status.StatusToolDescriptor` |
| **Source** | `gallery/.../status/` |

**SDK APIs:** `ToolComponent.Status`, `shouldShowCoordinates`, `shouldShowAzimuth`.

**Verify:** Status strip visible at bottom → host coordinates and azimuth update with map interaction.

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

### EndBar

| | |
|---|---|
| **Purpose** | Action, toggle, and menu buttons on the map's EndBar (each with a distinct icon) |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.endbar.EndBarSampleToolDescriptor` |
| **Source** | `gallery/.../endbar/` |

**SDK APIs:** `AbstractTool.endBar`, `EndBarActionButton`, `EndBarToggleButton`, `EndBarMenuButton`, `EndBarMenuScope.Checkable`, `EndBarMenuScope.Slider`.

**Verify:** Three distinct icons appear on the end bar → action fires toast → toggle state reflected in window → menu items and slider work.

---

## Section 3 — Map Underlay

Samples: UNDERLAY

### Underlay

| | |
|---|---|
| **Purpose** | Full-screen composable layer rendered behind the map |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.underlay.UnderlayToolDescriptor` |
| **Source** | `gallery/.../underlay/` |

**SDK APIs:** `ToolComponent.Underlay`, `requiredComponent`, `AbstractTool.endBar`, `EndBarActionButton`, `ToolManager.deactivate`.

**Verify:** Semi-transparent underlay visible behind map content; close via EndBar action.

---

## Section 4 — Panel Windows

Samples: WINDOW_SINGLE_SCREEN, WINDOW_MULTI_SCREEN, MAP_WINDOW

### Window — Single Screen

| | |
|---|---|
| **Purpose** | Minimal single-screen window tool with a ViewModel-backed counter |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.window.singlescreen.WindowSingleScreenToolDescriptor` |
| **Source** | `gallery/.../window/singlescreen/` |

**SDK APIs:** `ToolComponent.Window`, `requiredComponent`, `WindowScaffold`, `BackNavTopAppBar`, `diViewModel()`, `showToast`.

**Verify:** Window opens → **Increment** increases counter → **Reset** resets counter and shows toast.

---

### Window — Multi-Screen

| | |
|---|---|
| **Purpose** | Multi-screen window with tool-scoped DI and persisted settings |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.window.multiscreen.WindowMultiScreenToolDescriptor` |
| **Source** | `gallery/.../window/multiscreen/` |

**SDK APIs:** `AppNavHost`, `Route`, navigation transitions, `BackNavTopAppBar`, `subDI { import(module) }`, tool-scoped `SharedPreferences`.

**Verify:** Home screen → navigate to Settings → toggle → navigate back → description visibility matches toggle.

---

### Window — Secondary Map

| | |
|---|---|
| **Purpose** | Embedded secondary map inside a window panel (`ToolComponent.MapWindow`) |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.window.map.MapWindowToolDescriptor` |
| **Source** | `gallery/.../window/map/` |

**SDK APIs:** `ToolComponent.MapWindow`, embedded `MapView`, `MapController`, `MapWindow.mapEndBarButtons`, `MapWindow.navBarContent`, `MapWindow.focusCameraOn`.

**Verify:** Window opens with embedded map → zoom in/out work → mode selector switches view → Focus camera button moves map.

---

## Section 5 — Panel State

Samples: PANEL_STATE

### Panel State (PanelManager)

| | |
|---|---|
| **Purpose** | Open, close, and observe the panel via `PanelManager` |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.panelstate.PanelStateToolDescriptor` |
| **Source** | `gallery/.../panelstate/` |

**SDK APIs:** `PanelManager.openPanel(PanelState.Opened.Half)`, `PanelManager.openPanel(PanelState.Opened.Full())`, `PanelManager.closePanel()`, `PanelManager.panelState: StateFlow<PanelState>`.

**Verify:** Window opens → **Open Half** opens panel to half → **Open Full** expands → **Close** closes → current state label updates live.

---

## Section 6 — Tool Management

Samples: TOOL_MANAGEMENT

### Tool Management (ToolManager)

| | |
|---|---|
| **Purpose** | Activate, deactivate, check active state, and bring a tool window to front |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.toolmanagement.ToolManagementToolDescriptor` |
| **Source** | `gallery/.../toolmanagement/` |

**SDK APIs:** `ToolManager.activate`, `ToolManager.deactivate`, `ToolManager.isActive`, `ToolManager.activeTools: StateFlow`, `ToolManager.showComponent`, `ToolManager.FLAG_COMPONENT_ON_TOP`.

**Verify:** Activate Map tool → active list updates → **Show Map Window** brings Map window forward → deactivate Map tool → list updates again.

---

## Section 7 — UI Components Catalog

Samples: UI_CATALOG

### UI Catalog

| | |
|---|---|
| **Purpose** | Navigable catalog of promoted public SDK form and UI components |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.uicatalog.UiCatalogToolDescriptor` |
| **Source** | `gallery/.../uicatalog/` |

**SDK APIs:** `InlineMessage`, `HeaderField`, `ExpandableField`, `FormFieldBox`, `NestedForm`, `HostilitySelector`, buttons, inputs, selection, feedback, revealable lists, `AppNavHost`, `WindowScaffold`.

**Verify:** Launch → component list opens → tap a component → detail screen shows each documented state → back navigation returns to list.

---

## Section 8 — Dialogs

Samples: DIALOG

### Dialog

| | |
|---|---|
| **Purpose** | All four `ToolDialog` variants |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.dialog.DialogToolDescriptor` |
| **Source** | `gallery/.../dialog/` |

**SDK APIs:** `ToolDialog.Confirmation`, `.Destructive`, `.Info`, `.Custom`, `AbstractTool.showDialog()`, `dismissDialog()`.

**Verify:** Four buttons each open the correct dialog type; confirm and dismiss work for every variant.

---

## Section 9 — Data Management

Samples: MODEL, STORAGE

### Model (CommonModelInteractor)

| | |
|---|---|
| **Purpose** | Observe, select, and unselect BCM models via domain interactor; read-only awareness |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.model.ModelToolDescriptor` |
| **Source** | `gallery/.../model/` |

**SDK APIs:** `CommonModelInteractor` (`getAllModels`, `selectedModel`, `userModel`, `selectModel`, `unselectModel`), `isReadOnly`, `diViewModel()`.

**Verify:** Model list populated → tap row to select → **Unselect** clears selection → read-only banner shown when `isReadOnly` is true.

---

### Storage (CommonSessionStorageInteractor)

| | |
|---|---|
| **Purpose** | Display session directory paths; write and read a file off the main thread |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.storage.StorageToolDescriptor` |
| **Source** | `gallery/.../storage/` |

**SDK APIs:** `CommonSessionStorageInteractor.getRootDirectoryPath()`, `CommonSessionStorageInteractor.getUserDirectoryPath()`, `Dispatchers.IO`, `viewModelScope.launch`, `File.writeText`, `File.readText`.

**Verify:** Window shows root and user directory paths → **Write File** writes `gallery_sample.txt` → **Read File** reads it back and displays contents → **Read** before **Write** shows "file not found" hint rather than crashing.

---

## Section 10 — Lifecycle & Services

Samples: SERVICE

### Service (AbstractToolService)

| | |
|---|---|
| **Purpose** | Session-scoped tool service with shared DI state and list badge counter |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.service.ServiceToolDescriptor` |
| **Source** | `gallery/.../service/` |

**SDK APIs:** `ToolDescriptor.createService`, `AbstractToolService.toolSubDI`, `ToolNotificationManager.counter`.

**Verify:** Start session → service ticks in background → tool list badge increments → open tool window → event counter matches badge.

---

## Section 11 — Resources & Isolation

Samples: RESOURCES_CONFIG, RESOURCES_MATERIAL, RESOURCES_COLLISION, NATIVE_CROSS_APK

### Resources / Config

| | |
|---|---|
| **Purpose** | App language, night mode, config-qualified resources, custom font, raw file |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.resources.config.ConfigToolDescriptor` |
| **Source** | `gallery/.../resources/config/` |

**SDK APIs:** Configuration-qualified `values` / `values-night` / `values-uk`, `FontFamily(Font(R.font.*))`, `openRawResource(R.raw.*)`.

**Verify:** Toggle dark mode → mode string/icon updates → change locale to Ukrainian → all strings localize.

---

### Resources / Material

| | |
|---|---|
| **Purpose** | Plugin-compiled Material 2 widgets with composition fallback |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.resources.material.MaterialToolDescriptor` |
| **Source** | `gallery/.../resources/material/` |

**SDK APIs:** Plugin-local M2 (`Snackbar`, `AlertDialog`, `DropdownMenu`, `Slider`), `CompositionFallbackContext`, `FallbackResources`.

**Verify:** Window opens without crash → all four widget demos interactive.

---

### Resources / Collision

| | |
|---|---|
| **Purpose** | Plugin-first `R.string` resolution when names collide with host |
| **Descriptor** | `vision.combat.c4.ds.sample.gallery.resources.collision.CollisionToolDescriptor` |
| **Source** | `gallery/.../resources/collision/` |

**SDK APIs:** Plugin `R.string.settings` vs host `R.string.settings` — plugin value wins.

**Verify:** Window shows plugin-specific settings string, not the host default.

---

### Native / Cross-APK (`:isolation`)

| | |
|---|---|
| **Purpose** | Per-APK ClassLoader, `nativeLibraryDir` `.so`, plugin `AssetManager`, cross-APK activation |
| **Descriptor** | `vision.combat.c4.ds.sample.isolation.nativelib.NativeToolDescriptor` |
| **Source** | `isolation/.../nativelib/` |
| **Native** | `isolation/src/main/cpp/` (CMake → `libisolation_jni.so`) |

**SDK APIs:** `ToolManager.resolveToolId(fqcn)`, `ToolContext.assets`, `System.loadLibrary`, JNI from plugin `nativeLibraryDir`.

**Verify:**

1. Install both `:gallery` and `:isolation` APKs (see **Details** on the hub card for install commands).
2. Open Sample Gallery → Resources & Isolation → **Native / Cross-APK** → tap to launch.
3. Window shows asset content prefix and JNI result `isolation-jni/1.0`.

---

## i18n check

All user-visible strings exist in `values/` and `values-uk/`. After switching system locale to Ukrainian, open each sample from the hub and confirm no English leaks (except intentional code-level debug labels).

---

## Manual upgrade test

Not tied to a single tool — validates host behavior across APK updates:

1. Install `:gallery` with `versionCode = 1`. Open **Window — Multi-Screen**, set a preference.
2. Bump `versionCode` in [`gallery/build.gradle.kts`](../gallery/build.gradle.kts), rebuild, reinstall.
3. Confirm SharedPreferences state survived and hub still lists all samples.

See [Plugin isolation — case (d)](plugin-isolation.md#case-d-pinned-state-survives-versioncode-bump).
