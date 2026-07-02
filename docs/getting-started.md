# Getting started

**[← README](../README.md)** · **[Samples guidebook](samples-catalog.md)** · **[Plugin isolation](plugin-isolation.md)**

This guide covers prerequisites, Gradle setup, tool integration, and how to build and install the sample APKs from Android Studio.

---

## Requirements

To build and run these samples you need:

1. **[ComBat 4 Dismounted Soldier](https://play.google.com/store/apps/details?id=vision.combat.c4.ds)** installed on a device or emulator.
2. **Maven SDK repository access** — request credentials by writing to [support@combat.vision](mailto:support@combat.vision).
3. **Matching SDK version** — this repo pins `c4ds-sdk` in [`gradle/libs.versions.toml`](../gradle/libs.versions.toml). Check [Nexus](https://nexus.combat.vision/#browse/browse:maven-sdk:vision%2Fcombat%2Fc4ds-sdk) for the latest release (login with the same credentials).
4. **Binary-compatible toolchain** — use the same Kotlin version as the host app:

   ```
   kotlin = "2.4.0"
   ```

   Compose is provided by Kotlin's bundled K2 Compose compiler plugin (`org.jetbrains.kotlin.plugin.compose`);
   this repo does not pin a separate Compose runtime version. Mismatching the Kotlin version can cause
   subtle runtime failures in plugin UI.

5. **NDK + CMake** — required only when building `:isolation` (native `.so` smoke test).

| Item | This repo |
|---|---|
| Host app | ComBat 4 DS |
| SDK | `c4ds-sdk` `0.5.0` (see `gradle/libs.versions.toml`) |
| Kotlin | `2.4.0` |
| AGP | `9.2.1` |
| JVM | 17 |

---

## Tool screen layout

The host app divides the screen into **tool components**. Your tool declares which components it needs; the host renders them in fixed regions:

| Component | Region | Typical use |
|---|---|---|
| **Overlay** | Red — over the main map | Heads-up display, cursor readouts |
| **Status** | Blue — bottom strip | Non-critical info; can be expandable |
| **Window** | Green — side/bottom panel | Full screens with optional nested navigation |
| **MapWindow** | Variant of Window | Embedded secondary map inside the panel |
| **Underlay** | Full area under the main map | AR and background layers |
| **End bar** | Yellow — right edge of map | Custom action/toggle/menu buttons via `endBarButtons` |

<img width="1000" alt="Tool component screen layout" src="https://github.com/user-attachments/assets/bdf78782-9206-4cf8-9c1b-c5328eb6e3f8" />

Every sample in `:gallery` targets one or more of these regions. Open **Sample Gallery** from the host launcher, then use **Launch** on any card to try it.

---

## Gradle setup

### 1. Nexus credentials

Add to `~/.gradle/gradle.properties` (or the project root `gradle.properties`):

```properties
c4ds_sdk_username=<username>
c4ds_sdk_password=<password>
```

### 2. Maven repository

The root [`build.gradle.kts`](../build.gradle.kts) already configures the SDK repository for all subprojects:

```kotlin
subprojects {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://nexus.combat.vision/repository/maven-sdk/")
            credentials {
                username = System.getProperty("c4ds_sdk_username")
                    ?: rootProject.properties["c4ds_sdk_username"].toString()
                password = System.getProperty("c4ds_sdk_password")
                    ?: rootProject.properties["c4ds_sdk_password"].toString()
            }
        }
        mavenLocal()
    }
}
```

When starting a **new** tool project, copy this block into your root `build.gradle.kts`.

### 3. Module dependencies

Each sample module uses:

```kotlin
dependencies {
    compileOnly(libs.combat.ds.sdk)
    runtimeOnly(libs.combat.ds.sdk.runtime)
}
```

- `compileOnly` — SDK types and Compose APIs at compile time.
- `runtimeOnly` — minimal runtime shim loaded by the host when your tool activates.

The host app supplies Kotlin stdlib, Compose, and most AndroidX artifacts. Do **not** bundle duplicate runtime libraries in your plugin APK.

---

## Integration guide

External tools are plain Android **Application** modules with **no Activity**. The host discovers tools via manifest metadata.

### Step 1 — Subclass `AbstractTool`

Example: [`WindowSingleScreenTool.kt`](../gallery/src/main/kotlin/vision/combat/c4/ds/sample/gallery/window/singlescreen/WindowSingleScreenTool.kt)

```kotlin
internal class WindowSingleScreenTool(
    toolContext: ToolContext,
    descriptor: ToolDescriptor,
    di: DI,
    params: Bundle?,
) : AbstractTool(toolContext, descriptor, di, params) {

    override val window: ToolComponent.Window by requiredComponent {
        WindowSingleScreenWindow()
    }
}
```

Declare only the components your tool needs (`window`, `overlay`, `status`, `mapWindow`, etc.).

### Step 2 — Subclass `ToolDescriptor`

Example: [`WindowSingleScreenToolDescriptor.kt`](../gallery/src/main/kotlin/vision/combat/c4/ds/sample/gallery/window/singlescreen/WindowSingleScreenToolDescriptor.kt)

```kotlin
class WindowSingleScreenToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.window_single_screen_tool_name
    override val iconResId: Int = R.drawable.ic_window

    // Gallery samples hide from the host launcher; only the hub uses CATEGORY_LAUNCHER.
    override val categories: List<String> = emptyList()

    override fun createTool(
        toolContext: ToolContext,
        di: DI,
        params: Bundle?,
    ): AbstractTool = WindowSingleScreenTool(toolContext, this, di, params)
}
```

- `nameResId` / `iconResId` — shown in the host **Tools** list (when `categories` includes `CATEGORY_LAUNCHER`).
- `createTool` — factory invoked by the host when the tool is activated.

### Step 3 — Register descriptors in `combat_tools.xml`

Example: [`gallery/src/main/res/xml/combat_tools.xml`](../gallery/src/main/res/xml/combat_tools.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<combat-tools>
    <tool-descriptor name="vision.combat.c4.ds.sample.gallery.window.singlescreen.WindowSingleScreenToolDescriptor" />
</combat-tools>
```

List every `ToolDescriptor` FQCN your APK provides.

### Step 4 — Declare metadata in `AndroidManifest.xml`

```xml
<application ...>
    <meta-data
        android:name="vision.combat.c4.ds.sdk.DECLARED_TOOLS"
        android:resource="@xml/combat_tools" />
</application>
```

### Step 5 — Build and install

See [Building from Android Studio](#building-from-android-studio) below.

### Hub-only launcher visibility

In this repo, only **Sample Gallery** (`CatalogToolDescriptor`) sets `categories = listOf(CATEGORY_LAUNCHER)`. All other gallery tools use `categories = emptyList()` and are opened from the hub via `ToolManager.activate<T>()`. Copy this pattern when you want a single entry point for multiple tools in one APK.

---

## Building from Android Studio

Your module does **not** need an Activity. To run/debug:

1. Select **Nothing** under **Launch options**.
2. Enable **Always install with package manager** — otherwise the host may not see updated APKs.

<img width="500" alt="Run configuration: Nothing launch, Always install with package manager" src="https://github.com/user-attachments/assets/726d2066-f4d9-48d7-9aea-a1b14727e427" />

### Command line

From the repo root:

```bash
./gradlew :gallery:assembleRelease :isolation:assembleRelease
adb install -r gallery/build/outputs/apk/release/gallery-release.apk
adb install -r isolation/build/outputs/apk/release/isolation-release.apk
```

After install, ComBat 4 refreshes the **Tools** list. You usually see changes immediately after tool re-activation; if not, force-stop and restart the host app.

<img width="1000" alt="Installed tools in ComBat 4 launcher" src="https://github.com/user-attachments/assets/845f8c0c-d798-48d1-87c2-5a2cb39d95ed" />

### Using the Sample Gallery

1. Install `:gallery` (required) and optionally `:isolation` (for cross-APK native sample).
2. Open ComBat 4 → **Tools** → **Sample Gallery**.
3. Browse samples by section; tap **Launch** or **Details** on any card.
4. The **Native / Cross-APK** card is enabled only when `:isolation` is installed.

See the [Samples guidebook](samples-catalog.md) for per-sample screenshots, SDK APIs, and verification steps.
