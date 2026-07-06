# c4ds Tool Samples

<img width="1000" alt="Sample Gallery hub in ComBat 4" src="https://github.com/user-attachments/assets/bf1b3f82-b4e5-46ed-bfe9-7cb2cdddd0f1" />

**A copy-pasteable reference for every public SDK surface exposed to external ComBat 4 Dismounted
Soldier (C4DS) tools** — 24 runnable samples across 13 categories, launched from an in-app
**Sample Gallery** hub.

[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-7F52FF?logo=kotlin)](gradle/libs.versions.toml)
[![AGP](https://img.shields.io/badge/AGP-9.2.1-3DDC84?logo=android)](gradle/libs.versions.toml)
[![c4ds-sdk](https://img.shields.io/badge/c4ds--sdk-0.5.0-blue)](gradle/libs.versions.toml)
[![minSdk](https://img.shields.io/badge/minSdk-26-brightgreen)](gallery/build.gradle.kts)
[![License](https://img.shields.io/badge/license-TBD-lightgrey)](#disclaimer)

---

## What this demonstrates

- **Every tool component** — `Overlay`, `Status`/`ExpandableStatus`, `Window`, `MapWindow`,
  `Underlay`, and `EndBar` — each with a runnable, minimal sample.
- **Domain interactors** (`CommonMapInteractor`, `CommonModelInteractor`,
  `CommonSessionStorageInteractor`, `CommonLocaleSettingsInteractor`) used the way a real tool would.
- **Tool lifecycle & management** — `AbstractToolService`, `ToolManager`, `PanelManager`, and
  session-scoped storage (files, `SharedPreferences`, Room).
- **UI building blocks** — the promoted public SDK Compose component catalog plus all four
  `ToolDialog` variants.
- **Plugin isolation** — per-APK `ClassLoader`, resource resolution, Material 2 composition
  fallback, native `.so` loading, and cross-APK tool activation.
- **A single hub pattern** — one launcher-visible tool activates 21 hidden tools via `ToolManager`;
  copy it for multi-tool APKs.

---

## Quick start

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

Build and install:

```bash
./gradlew :gallery:assembleRelease :isolation:assembleRelease
adb install -r gallery/build/outputs/apk/release/gallery-release.apk
adb install -r isolation/build/outputs/apk/release/isolation-release.apk
```

Neither APK declares an Activity. Install both, launch ComBat 4, then open **Sample Gallery** from
the Tools list. Full setup: **[Getting started](docs/guides/getting-started.md)**.

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

<img width="1000" alt="Tool component screen layout" src="https://github.com/user-attachments/assets/eb7e727c-1716-4fd8-8cbf-c9be5ae16990" />

Details: **[Getting started → Tool screen layout](docs/guides/getting-started.md#tool-screen-layout)**

---

## Sample catalog

All 24 samples — with screenshots, SDK APIs, source paths, and verification steps — live in the
**[Samples guidebook](docs/guides/samples-catalog.md)**, one collapsible section per category in on-screen
order. Registry source of truth:
[`CatalogEntry.kt`](gallery/src/main/kotlin/vision/combat/c4/ds/sample/gallery/catalog/ui/CatalogEntry.kt).

1. **[Map View](docs/guides/samples-catalog.md#section-1-map-view)** — map taps, WorldWind renderables, and the `CommonMapInteractor` API · 3 samples
2. **[Map Overlays](docs/guides/samples-catalog.md#section-2-map-overlays)** — overlays, status bars, expandable status, EndBar buttons, and default-overlay replace/restore · 5 samples
3. **[Map Underlay](docs/guides/samples-catalog.md#section-3-map-underlay)** — composables rendered behind the map layer in AR mode · 1 sample
4. **[Panel Windows](docs/guides/samples-catalog.md#section-4-panel-windows)** — single-screen, multi-screen, and secondary-map panel windows · 3 samples
5. **[Panel Management](docs/guides/samples-catalog.md#section-5-panel-management)** — open, close, and observe panel state via `PanelManager` · 1 sample
6. **[UI Components Catalog](docs/guides/samples-catalog.md#section-6-ui-components-catalog)** — promoted public SDK components: form fields, buttons, inputs, and more · 1 sample
7. **[Tool Dialogs](docs/guides/samples-catalog.md#section-7-tool-dialogs)** — `ToolDialog` variants: Confirmation, Destructive, Info, and Custom · 1 sample
8. **[Tool Management](docs/guides/samples-catalog.md#section-8-tool-management)** — activate, deactivate, and inspect tools via `ToolManager` · 1 sample
9. **[Model Management](docs/guides/samples-catalog.md#section-9-model-management)** — `CommonModelInteractor` create/consume/commit, symbol keys, and selection events · 1 sample
10. **[Data Management](docs/guides/samples-catalog.md#section-10-data-management)** — isolated file I/O, plugin-scoped `SharedPreferences`, and Room · 1 sample
11. **[Lifecycle & Services](docs/guides/samples-catalog.md#section-11-lifecycle-services)** — a session `AbstractToolService`, unread badge, and live lifecycle log · 1 sample
12. **[Resources & Isolation](docs/guides/samples-catalog.md#section-12-resources-isolation)** — config-qualified resources, M2 widgets, `R.string` collision, and native/cross-APK · 4 samples
13. **[Architecture](docs/guides/samples-catalog.md#section-13-architecture)** — a multi-module tool (domain/data/app) launched from the hub via cross-APK activation · 1 sample

---

## Documentation

| Document | Contents |
|---|---|
| **[Getting started](docs/guides/getting-started.md)** | Requirements, Gradle/Nexus setup, tool screen layout, integration steps, Android Studio run config |
| **[Samples guidebook](docs/guides/samples-catalog.md)** | Every sample: screenshot, description, SDK APIs, source path, verification steps |
| **[Plugin isolation](docs/guides/plugin-isolation.md)** | Asset/JNI smoke tests, isolation cases (a–h), cross-APK activation |

Questions or issues: [open an issue](https://github.com/ComBatVision/c4ds-tool-samples/issues) or
email [support@combat.vision](mailto:support@combat.vision).

---

## Disclaimer

This is an early version and may contain bugs or incomplete features. License: **TBD**.
Feedback and contributions are welcome — if you encounter any issues, please
[open an issue](https://github.com/ComBatVision/c4ds-tool-samples/issues).
