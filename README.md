# c4ds Tool Samples

This repository contains **two sample plugin APKs** that cover the full public SDK surface for external ComBat 4 Dismounted Soldier tool authors. Each sample is a copy-pasteable reference implementation you can launch from an in-app **Sample Gallery** hub.

<img width="1000" alt="Sample Gallery hub in ComBat 4" src="https://github.com/user-attachments/assets/06c47964-0ebf-4cf3-80dc-6ac2a0639202" />

## Disclaimer

This is an early version and may contain bugs or incomplete features.  
Feedback and contributions are welcome!  
If you encounter any issues, please [open an issue](https://github.com/ComBatVision/c4ds-tool-samples/issues).

---

## Demo

[![Demo video — click to watch on YouTube](https://github.com/user-attachments/assets/e077a04b-35c4-4d77-bd7b-672d552a4f26)](https://youtu.be/6AOOwTl_N9Y)

---

## Documentation

| Document | Contents |
|---|---|
| **[Getting started](docs/getting-started.md)** | Requirements, Gradle/Nexus setup, tool screen layout, integration steps, Android Studio run config |
| **[Samples catalog](docs/samples-catalog.md)** | Every sample: purpose, SDK APIs, source paths, verification steps |
| **[Plugin isolation](docs/plugin-isolation.md)** | Asset/JNI smoke tests, isolation cases (a–h), cross-APK activation |

---

## Quick start

### Prerequisites

| Item | Requirement |
|---|---|
| Host app | [ComBat 4 DS](https://play.google.com/store/apps/details?id=vision.combat.c4.ds) |
| Maven access | [support@combat.vision](mailto:support@combat.vision) → [Nexus SDK](https://nexus.combat.vision/#browse/browse:maven-sdk:vision%2Fcombat%2Fc4ds-sdk) |
| SDK | `c4ds-sdk` `0.5.0` (see `gradle/libs.versions.toml`) |
| Kotlin / Compose | `2.4.0` / `1.11.2` — must match host for binary compatibility |
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

Neither APK declares an Activity. Install both, launch ComBat 4, then open **Sample Gallery** from the Tools list.

---

## Tool screen layout

The host divides the screen into tool components your tool can declare:

| Component | Use |
|---|---|
| **Overlay** (red) | Heads-up content over the main map |
| **Status** (blue) | Bottom info strip; expandable variant available |
| **Window** (green) | Side/bottom panel screens with optional navigation |
| **MapWindow** | Window variant with embedded secondary map |
| **Underlay** | Full layer under the main map (e.g. AR) |
| **End bar** (yellow) | Custom buttons on the map's right edge |

<img width="1000" alt="Tool component screen layout" src="https://github.com/user-attachments/assets/109fb109-a89b-4a34-ac54-ea7ab66966dc" />

Details: **[Getting started → Tool screen layout](docs/getting-started.md#tool-screen-layout)**

---

## Repository layout

```
c4ds-tool-samples/
├── gallery/          # Main APK — Sample Gallery hub + 17 feature samples
├── isolation/        # Second APK — JNI + asset isolation (cross-APK activation)
└── docs/             # Author documentation (this README links there)
```

Only **Sample Gallery** (`CatalogToolDescriptor`) appears in the host launcher. All other gallery tools use `categories = emptyList()` and launch from the hub via `ToolManager`.

---

## Sample overview

### `:gallery` — 17 feature samples + hub

| Sample | Section | SDK surface |
|---|---|---|
| Window — Single Screen | Windows | `ToolComponent.Window`, `WindowScaffold`, `diViewModel()`, `showToast` |
| Window — Multi-Screen | Windows | `AppNavHost`, `Route`, `subDI`, `SharedPreferences` |
| Window — Secondary Map | Windows | `ToolComponent.MapWindow`, embedded `MapView`, `MapController` |
| Map | Map | `AbstractMapTool`, `RenderableLayer`, `SelectDragCallback`, `ToolComponent.Status` |
| Underlay | Map | `ToolComponent.Underlay` |
| Overlay | Map Overlays & Status | `ToolComponent.Overlay`, `CommonMapInteractor`, `CommonModelInteractor` |
| Status | Map Overlays & Status | `ToolComponent.Status`, `shouldShowCoordinates`, `shouldShowAzimuth` |
| Expandable Status | Map Overlays & Status | `ToolComponent.ExpandableStatus`, `isExpanded`, `shouldShowAbove` |
| End Bar | Map Overlays & Status | `EndBarActionButton`, `EndBarToggleButton`, `EndBarMenuButton` |
| UI Catalog | UI Components | `InlineMessage`, `HeaderField`, `ExpandableField`, `FormFieldBox`, `NestedForm`, `HostilitySelector`, buttons, inputs, selection, feedback, lists |
| Model | Model & Map Data | `CommonModelInteractor` CRUD, `isReadOnly` |
| Map Interactor | Model & Map Data | `CommonMapInteractor` camera, display mode, reticle, cursor pin, focus, magnetic corrections |
| Service | Lifecycle & Services | `AbstractToolService`, `ToolNotificationManager` |
| Resources / Config | Resources & Isolation | Locale, night, config-qualified resources, font, raw |
| Resources / Material | Resources & Isolation | Plugin M2 widgets, `CompositionFallbackContext` |
| Resources / Collision | Resources & Isolation | Plugin-first `R.string` resolution |

Per-sample verification and source paths: **[Samples catalog](docs/samples-catalog.md)**

### `:isolation` — cross-APK native sample

| Sample | SDK surface |
|---|---|
| Native / Cross-APK | Per-APK `ClassLoader`, `nativeLibraryDir` `.so`, plugin `AssetManager`, `ToolManager.resolveToolId` |

Smoke tests and logcat expectations: **[Plugin isolation](docs/plugin-isolation.md)**

---

## Integration (summary)

1. Create an Android **Application** module (no Activity).
2. Add `compileOnly(libs.combat.ds.sdk)` + `runtimeOnly(libs.combat.ds.sdk.runtime)`.
3. Subclass `ToolDescriptor` + `AbstractTool`.
4. Create `res/xml/combat_tools.xml` listing descriptor FQCNs.
5. Add `vision.combat.c4.ds.sdk.DECLARED_TOOLS` meta-data to `AndroidManifest.xml`.

Step-by-step with code examples: **[Getting started → Integration guide](docs/getting-started.md#integration-guide)**

### Building from Android Studio

Select **Nothing** as launch option and enable **Always install with package manager**.

<img width="500" alt="Run configuration" src="https://github.com/user-attachments/assets/726d2066-f4d9-48d7-9aea-a1b14727e427" />

After install, your tool appears in the host Tools list (gallery shows only **Sample Gallery**):

<img width="1000" alt="Installed tools" src="https://github.com/user-attachments/assets/019a24eb-0fc9-46aa-b943-7139fb7857e2" />

---

## Plugin isolation (summary)

| Case | Sample | Proves |
|---|---|---|
| (a) | Resources / Material | M2 composition fallback |
| (b) | Resources / Collision | Plugin `R.string` over host |
| (c) | Resources / Config | Live locale/night reactivity |
| (d) | Manual upgrade test | State survives `versionCode` bump |
| (e) | Resources / Config | Plugin font + raw resources |
| (g) | End Bar | Plugin drawables in end bar |
| (h) | `:isolation` Native Tool | Cross-APK `.so`, assets, ClassLoader |

Full procedures: **[Plugin isolation](docs/plugin-isolation.md)**
