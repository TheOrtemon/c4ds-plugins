# c4ds Tool Samples

<img width="1000" alt="Sample Gallery hub in ComBat 4" src="https://github.com/user-attachments/assets/06c47964-0ebf-4cf3-80dc-6ac2a0639202" />

**A copy-pasteable reference for every public SDK surface exposed to external ComBat 4 Dismounted
Soldier (C4DS) tools** — 22 runnable samples across 12 categories, launched from an in-app
**Sample Gallery** hub.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-7F52FF?logo=kotlin)](gradle/libs.versions.toml)
[![AGP](https://img.shields.io/badge/AGP-9.2.1-3DDC84?logo=android)](gradle/libs.versions.toml)
[![c4ds-sdk](https://img.shields.io/badge/c4ds--sdk-0.5.0-blue)](gradle/libs.versions.toml)
[![minSdk](https://img.shields.io/badge/minSdk-26-brightgreen)](gallery/build.gradle.kts)
[![License](https://img.shields.io/badge/license-TBD-lightgrey)](#disclaimer)

[![Demo video — click to watch on YouTube](https://github.com/user-attachments/assets/e077a04b-35c4-4d77-bd7b-672d552a4f26)](https://youtu.be/6AOOwTl_N9Y)

---

## What this demonstrates

- **Every tool component** the host exposes — `Overlay`, `Status`/`ExpandableStatus`, `Window`,
  `MapWindow`, `Underlay`, and `EndBar` — each with a runnable, minimal sample.
- **Domain interactors** (`CommonMapInteractor`, `CommonModelInteractor`,
  `CommonSessionStorageInteractor`, `CommonLocaleSettingsInteractor`) used the way a real tool would.
- **Tool lifecycle & management** — `AbstractToolService`, `ToolManager` activation/deactivation,
  `PanelManager` state, and session-scoped storage (files, `SharedPreferences`, Room).
- **UI building blocks** — the promoted public SDK Compose component catalog (form fields, buttons,
  inputs, selection, feedback, lists) plus all four `ToolDialog` variants.
- **Plugin isolation** — per-APK `ClassLoader`, `R.string`/config-qualified resource resolution,
  Material 2 composition fallback, native `.so` loading, and cross-APK tool activation.
- **A single hub pattern** — one launcher-visible tool (`Sample Gallery`) that activates 21 other
  hidden tools via `ToolManager`, so you can copy this pattern for multi-tool APKs.

---

## Quick start

### Prerequisites

| Item | Requirement |
|---|---|
| Host app | [ComBat 4 DS](https://play.google.com/store/apps/details?id=vision.combat.c4.ds) |
| Maven access | [support@combat.vision](mailto:support@combat.vision) → [Nexus SDK](https://nexus.combat.vision/#browse/browse:maven-sdk:vision%2Fcombat%2Fc4ds-sdk) |
| SDK | `c4ds-sdk` `0.5.0` (see `gradle/libs.versions.toml`) |
| Kotlin | `2.4.0` — must match host for binary compatibility |
| Compose | TBD — not pinned in this repo; use the Kotlin K2 Compose compiler shipped with `kotlin = "2.4.0"` |
| NDK + CMake | Only for `:isolation` |

Add Nexus credentials to `~/.gradle/gradle.properties`:

```properties
c4ds_sdk_username=<your-username>
c4ds_sdk_password=<your-password>
```

Full setup: **[Getting started → Gradle setup](docs/getting-started.md#gradle-setup)**

### Build and install

```bash
./gradlew :gallery:assembleRelease :isolation:assembleRelease
adb install -r gallery/build/outputs/apk/release/gallery-release.apk
adb install -r isolation/build/outputs/apk/release/isolation-release.apk
```

Neither APK declares an Activity. Install both, launch ComBat 4, then open **Sample Gallery** from
the Tools list.

---

## Screen layout

The host divides the screen into tool components your tool declares:

| Component | Region | Use |
|---|---|---|
| **Overlay** | Red — over the main map | Heads-up content, cursor readouts |
| **Status** | Blue — bottom info strip | Non-critical info; `ExpandableStatus` variant available |
| **Window** | Green — side/bottom panel | Full screens with optional nested navigation |
| **MapWindow** | Variant of Window | Embedded secondary map inside the panel |
| **Underlay** | Full layer under the main map | AR and background layers |
| **End bar** | Yellow — map's right edge | Custom action/toggle/menu buttons |

<img width="1000" alt="Tool component screen layout" src="https://github.com/user-attachments/assets/109fb109-a89b-4a34-ac54-ea7ab66966dc" />

Details: **[Getting started → Tool screen layout](docs/getting-started.md#tool-screen-layout)**

---

## Sample catalog

All 22 samples, grouped by the 12 catalog sections (in on-screen order). Registry source of truth:
[`CatalogEntry.kt`](gallery/src/main/kotlin/vision/combat/c4/ds/sample/gallery/catalog/ui/CatalogEntry.kt).

| Section | Sample | Purpose | SDK APIs |
|---|---|---|---|
| Map View | Map | Handles map taps and draws a placemark on the tapped location using `AbstractMapTool` | `AbstractMapTool`, `onTerrainPicked`, `addRenderable`, `requestRedraw`, `ToolComponent.Status` |
| Map View | Renderables | Draws WorldWind renderables — point, polyline, polygon, circle, label — on the map | `addRenderable`/`removeRenderable`, WorldWind `Placemark`/`Path`/`Polygon`/`Ellipse`/`Label`, `requestRedraw` |
| Map View | Map Interactor | Live `CommonMapInteractor` readout: camera/lookAt, focus actions, display mode, cursor pin, geodetic corrections | `CommonMapInteractor`, `camera`, `lookAt`, `selectedPosition`, `mapDisplayMode`, `focusOnLocation`, `focusOnSector`, `getDeclination` |
| Map Overlays | Overlay | `ToolComponent.Overlay` composable reading cursor position and user model | `ToolComponent.Overlay`, `CommonMapInteractor.selectedPosition`, `CommonModelInteractor.userModel`, `CommonLocaleSettingsInteractor` |
| Map Overlays | Status | `ToolComponent.Status` with host coordinate/azimuth chrome flags | `ToolComponent.Status`, `shouldShowCoordinates`, `shouldShowAzimuth` |
| Map Overlays | Expandable Status | `ToolComponent.ExpandableStatus` collapsible panel above or below the strip | `ToolComponent.ExpandableStatus`, `isExpanded`, `shouldShowAbove`, `EndBarToggleButton` |
| Map Overlays | End Bar | Full EndBar API — action, toggle, and menu button with items and slider | `AbstractTool.endBar`, `EndBarActionButton`, `EndBarToggleButton`, `EndBarMenuButton`, `EndBarMenuScope.Checkable`, `EndBarMenuScope.Slider` |
| Map Underlay | Map Underlay | Full-screen composable rendered behind the map; host enables AR map mode while active | `ToolComponent.Underlay`, `requiredComponent`, `AbstractTool.endBar`, `ToolManager.deactivate` |
| Panel Windows | Single Screen Window | Minimal single-screen window with a ViewModel-backed counter (MVI + one-shot toast) | `ToolComponent.Window`, `WindowScaffold`, `BackNavTopAppBar`, `diViewModel()`, `showToast` |
| Panel Windows | Multi Screen Window | Multi-screen window using `AppNavHost`, tool-scoped DI, and `SharedPreferences` | `AppNavHost`, `Route`, `BackNavTopAppBar`, `subDI`, `SharedPreferences` |
| Panel Windows | Secondary Map Window | Embeds a secondary map inside a tool window via `ToolComponent.MapWindow` | `ToolComponent.MapWindow`, `MapView`, `MapController`, `MapWindow.mapEndBarButtons`, `MapWindow.focusCameraOn` |
| Panel Management | Panel Management | Open (Half/Full) and close the panel; observe live `PanelState` via `StateFlow` | `PanelManager.openPanel`, `PanelManager.closePanel`, `PanelManager.panelState`, `PanelState.Opened.Half`, `PanelState.Opened.Full` |
| Tool Management | Tool Management | Activate/deactivate/`isActive` against the Map sample; bring its window forward; observe `activeTools` | `ToolManager.activate`, `ToolManager.deactivate`, `ToolManager.isActive`, `ToolManager.activeTools`, `ToolManager.showComponent`, `ToolManager.FLAG_COMPONENT_ON_TOP` |
| UI Components Catalog | UI Catalog | Navigable catalog of promoted public SDK components across form fields and six UI groups | `InlineMessage`, `HeaderField`, `ExpandableField`, `FormFieldBox`, `NestedForm`, `HostilitySelector`, Buttons, TopAppBar, Inputs, Selection, Feedback, Lists |
| Tool Dialogs | Tool Dialogs | All four `ToolDialog` variants via `showDialog`/`dismissDialog` | `ToolDialog.Confirmation`, `.Destructive`, `.Info`, `.Custom`, `AbstractTool.showDialog`, `dismissDialog` |
| Model Management | Model Management | `CommonModelInteractor` list, create/consume/commit, symbol keys, selection & events | `CommonModelInteractor`, `getAllModels`, `createModel`, `consumeModel`, `commitModel`, `selectModel`, `unselectModel`, `isReadOnly`, `rememberSymbolPainter` |
| Data Management | Data Management | Isolated file I/O, plugin-scoped `SharedPreferences`, and an isolated Room database | `CommonSessionStorageInteractor`, `SharedPreferences`, Room, `Dispatchers.IO` |
| Lifecycle & Services | Lifecycle & Services | Session `AbstractToolService` doing background work, unread badge, live lifecycle log | `ToolDescriptor.createService`, `AbstractToolService`, `ToolNotificationManager.counter`, `onComponentShown`/`onComponentHidden`/`onDestroyRequested` |
| Resources & Isolation | Config-Qualified Resources | Locale (`values-uk`), night mode (`values-night`), plugin-compiled font, raw resource file | `stringResource`, `FontFamily(Font(R.font.*))`, `context.resources.openRawResource` |
| Resources & Isolation | M2 Widgets & Popup Isolation | Plugin-compiled M2 widgets and popup context isolation across a Compose window boundary | M2 `Scaffold`, `SnackbarHost`, `Slider`, `DropdownMenu`, `ProvideWindowContext`, `ToolAlertDialog`, `CompositionFallbackContext` |
| Resources & Isolation | R.string Collision | Plugin `R.string` values take priority over identically-named host strings | `CompositionFallbackContext`, `FallbackResources` |
| Resources & Isolation | Native / Cross-APK (`:isolation`) | Per-APK classloader separation, native `.so` from plugin `nativeLibraryDir`, plugin-scoped `AssetManager` | `ToolContext.assets`, `System.loadLibrary`, `ToolManager.resolveToolId`, `ToolComponent.Window` |

Per-sample verification steps: **[Samples catalog](docs/samples-catalog.md)**

---

## Screenshots

| | |
|---|---|
| ![Sample Gallery hub](https://github.com/user-attachments/assets/96a43531-c47f-4c7c-b940-805a2da1bd80) **Sample Gallery hub** — 12 category cards, each with title, description, and icon. | ![UI Catalog](https://github.com/user-attachments/assets/91a18762-557b-47cb-bc64-4ad180b84873) **UI Components Catalog** — promoted public SDK form fields and widgets. |
| ![Map renderables](https://github.com/user-attachments/assets/67294458-f48e-4b46-80d8-60d0ec2f7e00) **Map — Renderables** — WorldWind placemarks, paths, and polygons. | ![Window — Multi-Screen](https://github.com/user-attachments/assets/8f60b96a-3d0d-4319-92a7-fd27563df84d) **Window — Multi-Screen** — `AppNavHost` navigation with persisted settings. |
| ![Tool Dialogs](https://github.com/user-attachments/assets/e353622a-7878-4893-acce-41dd57fde70e) **Tool Dialogs** — all four `ToolDialog` variants. | ![Lifecycle & Services](https://github.com/user-attachments/assets/6260361d-da14-4e0e-b6e2-b1f264532f77) **Lifecycle & Services** — session `AbstractToolService` with a live lifecycle log. |
| ![Data Management](https://github.com/user-attachments/assets/bc66cc9e-575c-4f6a-985b-a43232b6b70e) **Data Management** — isolated file I/O, `SharedPreferences`, and Room. | ![Model Management](https://github.com/user-attachments/assets/688b9330-35ef-4829-a99a-fa15d413eca6) **Model Management** — `CommonModelInteractor` CRUD and selection. |

---

## Architecture

<details>
<summary><strong>Module tree (click to expand)</strong></summary>

```
c4ds-tool-samples/
├── gallery/                     # Main APK — Sample Gallery hub + 21 feature samples
│   └── src/main/kotlin/vision/combat/c4/ds/sample/gallery/
│       ├── catalog/             # Hub: CatalogSection, CatalogEntry, CatalogTool (launcher-visible)
│       ├── mapview/
│       │   ├── map/             # Map — AbstractMapTool, tap handling
│       │   ├── renderable/      # Renderables — WorldWind primitives
│       │   └── mapinteractor/   # Map Interactor — CommonMapInteractor
│       ├── mapoverlays/
│       │   ├── overlay/         # Overlay — ToolComponent.Overlay
│       │   ├── status/          # Status — ToolComponent.Status
│       │   ├── expandablestatus/# Expandable Status
│       │   └── endbar/          # End Bar — EndBar button API
│       ├── underlay/            # Map Underlay — ToolComponent.Underlay
│       ├── window/
│       │   ├── singlescreen/    # Single Screen Window
│       │   ├── multiscreen/     # Multi Screen Window — AppNavHost, subDI
│       │   └── map/             # Secondary Map Window — ToolComponent.MapWindow
│       ├── panelstate/          # Panel Management — PanelManager
│       ├── toolmanagement/      # Tool Management — ToolManager
│       ├── uicatalog/           # UI Components Catalog
│       ├── dialog/              # Tool Dialogs — ToolDialog variants
│       ├── model/               # Model Management — CommonModelInteractor
│       ├── storage/             # Data Management — files, SharedPreferences, Room
│       ├── service/             # Lifecycle & Services — AbstractToolService
│       └── resources/
│           ├── config/          # Config-Qualified Resources
│           ├── material/        # M2 Widgets & Popup Isolation
│           └── collision/       # R.string Collision
├── isolation/                   # Second APK — JNI + asset isolation (cross-APK activation)
│   └── src/main/kotlin/vision/combat/c4/ds/sample/isolation/
│       └── nativelib/           # Native / Cross-APK — NativeToolDescriptor
└── docs/                        # Author documentation (this README links there)
```

Only **Sample Gallery** (`CatalogToolDescriptor`) appears in the host launcher. All other gallery
tools use `categories = emptyList()` and launch from the hub via `ToolManager`.

</details>

---

## Tech stack

| Layer | Choice |
|---|---|
| Language | Kotlin `2.4.0` |
| UI | Jetpack Compose (K2 Compose compiler bundled with Kotlin `2.4.0`) |
| Build | Android Gradle Plugin `9.2.1`, Gradle `9.5.1` |
| SDK | `c4ds-sdk` / `c4ds-sdk-runtime` `0.5.0` |
| DI | Kodein (`subDI`, `diViewModel()`) |
| Persistence | Room `2.8.4`, `SharedPreferences`, plugin-scoped file storage |
| Annotation processing | KSP `2.3.9` |
| Native | NDK + CMake (`:isolation` only) |
| Desugaring | `desugar_jdk_libs` `2.1.5` |
| JVM target | 17 |
| `minSdk` / `targetSdk` / `compileSdk` | 26 / 37 / 37 |

---

## Docs & support

| Document | Contents |
|---|---|
| **[Getting started](docs/getting-started.md)** | Requirements, Gradle/Nexus setup, tool screen layout, integration steps, Android Studio run config |
| **[Samples catalog](docs/samples-catalog.md)** | Every sample: purpose, SDK APIs, source paths, verification steps |
| **[Plugin isolation](docs/plugin-isolation.md)** | Asset/JNI smoke tests, isolation cases (a–h), cross-APK activation |

Questions or issues: [open an issue](https://github.com/ComBatVision/c4ds-tool-samples/issues) or
email [support@combat.vision](mailto:support@combat.vision).

---

## Disclaimer

This is an early version and may contain bugs or incomplete features. License: **TBD**.
Feedback and contributions are welcome — if you encounter any issues, please
[open an issue](https://github.com/ComBatVision/c4ds-tool-samples/issues).
