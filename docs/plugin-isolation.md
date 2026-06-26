# Plugin isolation

External tool APKs run in an isolated classloader with their own resources, assets, and native libraries. The host must not leak its own `AssetManager`, `Resources`, or `nativeLibraryDir` into plugin code.

This repo exercises isolation through dedicated gallery samples and the separate `:isolation` APK. Use **external plugin APKs** for isolation testing — the bundled in-process `:c4ds-tool:template` runs on the host context and cannot validate plugin-only isolation.

---

## Quick smoke test (`:isolation`)

Build and install both APKs, then launch **Native / Cross-APK** from Sample Gallery.

### Asset isolation

`NativeTool` reads `assets/isolation/sample.txt` via `toolContext.assets` on start.

**Expected logcat** (tag `NativeTool`):

```
[ASSET SMOKE] Read 'isolation/sample.txt' from :isolation plugin. Content prefix: ...
```

**Failure** (host serving wrong `AssetManager`):

```
[ASSET SMOKE] FAILED — check ToolContext.getAssets() isolation
```

### Native `.so` isolation

`:isolation` ships `libisolation_jni.so` built from [`isolation/src/main/cpp/`](../isolation/src/main/cpp/). `NativeTool` calls `System.loadLibrary("isolation_jni")` and invokes `IsolationNative.nativeVersion()`.

**Expected logcat:**

```
[JNI SMOKE] nativeVersion() = 'isolation-jni/1.0' — .so loaded from plugin OK
```

**Failure:**

```
[JNI SMOKE] FAILED to load libisolation_jni.so — check nativeLibraryDir wiring
```

The host `ToolPackageCache` passes `nativeLibraryDir` as `libPath` to `PathClassLoader`, so `System.loadLibrary` finds the unpacked `.so` when `android:extractNativeLibs="true"` is set in the plugin manifest.

### Window UI

The native tool window displays:

- Asset read result (content prefix from plugin APK)
- JNI result string (`isolation-jni/1.0`)

---

## Isolation cases reference

| Case | Sample | What it proves |
|---|---|---|
| **(a)** | Resources / Material | M2 widgets compiled into plugin; `CompositionFallbackContext` + `FallbackResources` |
| **(b)** | Resources / Collision | Plugin `R.string` wins over host for same resource name |
| **(c)** | Resources / Config | Strings/drawables react to locale and night-mode changes |
| **(d)** | Manual (any persisted tool) | State survives `versionCode` bump on reinstall |
| **(e)** | Resources / Config | `R.font` and `R.raw` resolve from plugin APK |
| **(g)** | End Bar | `painterResource(R.drawable.*)` in `endBar { }` uses plugin resources |
| **(h)** | Native / Cross-APK (`:isolation`) | Separate APK ClassLoader, assets, and native library dir |

---

### Case (a): M2 composition fallback

**Sample:** Resources / Material (`MaterialTool`)  
**Source:** `gallery/.../resources/material/`

Plugin APKs compile their own Compose Material 2 dependency. The host provides a fallback composition context so plugin `@Composable` functions resolve theme and resources correctly.

**Verify:** Launch sample → `Snackbar`, `AlertDialog`, `DropdownMenu`, and `Slider` all render and respond without crash or missing theme errors.

---

### Case (b): R.string collision

**Sample:** Resources / Collision (`CollisionTool`)  
**Source:** `gallery/.../resources/collision/`

Both host and plugin define `R.string.settings` with different text. Plugin-first resolution must show the plugin value.

**Verify:** Window displays the plugin-specific settings string (defined in `gallery/src/main/res/values/strings.xml`), not the host string.

---

### Case (c): Config reactivity

**Sample:** Resources / Config (`ConfigTool`)  
**Source:** `gallery/.../resources/config/`

Plugin UI must recompose when host configuration changes (app language, uiMode/night).

**Verify:**

1. Open Resources / Config.
2. Toggle system dark mode → `config_mode` string and day/night icon update without restarting the tool.
3. Change **App language** to Ukrainian in Settings → all window strings switch to `values-uk`.

---

### Case (d): Pinned state survives versionCode bump

**Sample:** Manual procedure (not a dedicated tool)  
**Suggested tool:** Window — Multi-Screen (persists toggle in tool-scoped `SharedPreferences`)

**Procedure:**

1. Set `versionCode = 1` in [`gallery/build.gradle.kts`](../gallery/build.gradle.kts). Build and install.
2. Open **Window — Multi-Screen** → Settings → enable **Show description on Home**.
3. Bump `versionCode` to `2`. Rebuild and reinstall with `adb install -r` (same `applicationId`).
4. Reopen Window — Multi-Screen → confirm toggle is still enabled.
5. Confirm Sample Gallery hub lists all samples.

This validates descriptor re-registration and tool-scoped storage across package-manager updates.

---

### Case (e): Font + raw resource

**Sample:** Resources / Config (`ConfigTool`)  
**Source:** `gallery/.../resources/config/`

**Verify:** Window shows text rendered with plugin `R.font.sample_font` and displays content read from `R.raw.sample_note` via `openRawResource`. Both must resolve from the plugin APK, not the host.

---

### Case (g): EndBar Painter API

**Sample:** End Bar (`EndBarSampleTool`)  
**Source:** `gallery/.../endbar/`

**Verify:** End-bar buttons show plugin drawables loaded through `painterResource(R.drawable.*)` inside the `endBar { }` DSL. Icons must match plugin `res/drawable`, not host assets.

---

### Case (h): Cross-APK native `.so` + assets

**Sample:** Native Tool in `:isolation`  
**Source:** `isolation/.../nativelib/`

This case requires **two APKs**:

| APK | Role |
|---|---|
| `:gallery` | Hub; activates isolation tool via `ToolManager.resolveToolId("...NativeToolDescriptor")` |
| `:isolation` | Provides `NativeToolDescriptor` on a separate ClassLoader |

**Why a second APK:** Same-APK activation uses reified `activate<T>()` and shares one ClassLoader. Cross-APK resolution proves the host indexes tools from multiple installed plugin packages.

**Verify:** See [Quick smoke test](#quick-smoke-test-isolation) above.

**Implementation notes:**

- JNI class lives in package `nativelib` (not `native` — Java keyword).
- CMake project: [`isolation/build.gradle.kts`](../isolation/build.gradle.kts) + [`isolation/src/main/cpp/CMakeLists.txt`](../isolation/src/main/cpp/CMakeLists.txt).
- ABIs: `arm64-v8a`, `x86_64`.

---

## Implementing isolation in your own tool

### Assets

```kotlin
toolContext.assets.open("your/path/file.txt").use { stream ->
    // Must read from plugin AssetManager, not host
}
```

### Native code

1. Add NDK + CMake to your module's `build.gradle.kts` (see `:isolation` for template).
2. Place JNI sources under `src/main/cpp/`.
3. Load with `System.loadLibrary("your_lib")` — no absolute path needed when host wiring is correct.
4. Set `android:extractNativeLibs="true"` in the plugin manifest if required for your target SDK.

### Cross-APK activation from another plugin

When the target descriptor is not on your classpath:

```kotlin
val toolId = toolManager.resolveToolId("com.example.otherapk.TheirToolDescriptor")
toolId?.let { toolManager.activate(it, ToolManager.FLAG_COMPONENT_ON_TOP) }
```

Returns `null` if the other APK is not installed — disable UI accordingly (see `CatalogEntry` isolation entry).

---

## Related documentation

- [Samples catalog — Native / cross-APK](samples-catalog.md#native--cross-apk-isolation)
- [Getting started — integration](getting-started.md#integration-guide)
