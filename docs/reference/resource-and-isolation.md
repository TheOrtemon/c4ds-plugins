# Resource & isolation

**[← AGENTS.md](../../AGENTS.md)**

Your tool's APK runs with its own resources, assets, and (if you use them) native
libraries — separate from the host's. This doc covers what that means for how you read
resources/assets, load native code, and obtain session/user-scoped storage paths.

> **Sibling docs:** [Tool lifecycle & setup](tool-lifecycle-and-setup.md) ·
> [Data & domain](../architecture/data-and-domain.md)

> **Obfuscated classes need isolation too.** Resource/asset isolation is per-APK by
> construction, but release-build (R8) class names are not — without a unique
> `-repackageclasses` target your tool's obfuscated classes can collide with the host's. See
> [Getting started — Release builds and obfuscation](../guides/getting-started.md#release-builds-and-obfuscation).

---

## Resources are yours, config-reactive

Your plugin's own `res/` resources (`values/strings.xml`, `values-night/`, `values-uk/`,
drawables, fonts, raw files) resolve from your APK, not the host's — including when a
resource name happens to collide with one the host also defines. Standard Compose resource
APIs (`stringResource`, `painterResource`, `FontFamily(Font(R.font.*))`,
`context.resources.openRawResource(R.raw.*)`) work exactly as in any other Android/Compose
module; there's nothing extra to opt into.

- **Localize normally.** Ship every user-visible string in `values/` plus every locale you
  support (e.g. `values-uk/`). Your plugin UI recomposes when the host's app-language setting
  changes, the same way it would in a plain Compose app reacting to configuration change.
- **Night mode / other qualifiers work normally too** — `values-night/`, screen-size
  qualifiers, etc. all resolve against your plugin's own resource table and react to host
  configuration changes without extra wiring on your part.
- **Fonts and raw resources** compiled into your plugin (`R.font.*`, `R.raw.*`) resolve from
  your APK. Read them with the same APIs you'd use in any Compose app.

### Material widgets across the window boundary

If your tool renders through the SDK's `ToolDialog` variants and the promoted component
catalog, you generally don't need to think about composition context at all — use them as
documented. If you drop down to a raw Compose Material widget that opens its own window/
popup internally (a raw `DropdownMenu`, a hand-rolled `AlertDialog`), be aware that
`LocalContext` and similar composition locals can reset inside that popup's sub-composition.
The SDK's dialog/menu wrapper components exist specifically to save you from re-providing
context manually — prefer them over hand-rolled popups.

---

## Assets

Read your plugin's bundled assets through the `ToolContext` your `AbstractTool`/
`ToolDescriptor` is constructed with — not `Context.getAssets()` on some other context you
happen to have lying around:

```kotlin
toolContext.assets.open("your/path/file.txt").use { stream ->
    // reads from your plugin's own assets, not the host's
}
```

---

## Native libraries (NDK / JNI)

If your tool needs native code:

1. Add NDK + CMake to your module's `build.gradle.kts`:

   ```kotlin
   android {
       defaultConfig {
           ndk {
               abiFilters += listOf("arm64-v8a", "x86_64")
           }
           externalNativeBuild {
               cmake {
                   cppFlags += "-std=c++17"
               }
           }
       }
       externalNativeBuild {
           cmake {
               path = file("src/main/cpp/CMakeLists.txt")
               version = "3.22.1" // match whatever CMake version you have installed
           }
       }
       packaging {
           jniLibs {
               useLegacyPackaging = true
           }
       }
   }
   ```

   `arm64-v8a` covers production devices; `x86_64` covers emulators. Adjust to whatever ABIs
   you actually need to support.

2. Place JNI sources under `src/main/cpp/` with a `CMakeLists.txt` that builds your `.so`.

3. Load it the normal way — no absolute path needed:

   ```kotlin
   System.loadLibrary("your_lib")
   ```

`packaging { jniLibs { useLegacyPackaging = true } }` ensures your `.so` is extracted to your
plugin's own native library directory at install time, which is what makes plain
`System.loadLibrary` resolve it correctly for a plugin APK.

---

## Session-scoped storage

For file storage, `SharedPreferences`, and local databases, use the SDK's session-storage
domain interactor rather than ambient Android APIs, so your data is correctly scoped per
user/session and isolated to your plugin:

```kotlin
class MyToolInteractor(
    private val sessionStorage: CommonSessionStorageInteractor,
) {
    fun rootDir(): String = sessionStorage.getRootDirectoryPath()
    fun userDir(): String = sessionStorage.getUserDirectoryPath()
}
```

- `getRootDirectoryPath()` — a session-level directory.
- `getUserDirectoryPath()` — a directory scoped to the current user; this is what you should
  build a Room database file path from (see [Data & domain — Room](../architecture/data-and-domain.md#room)).

Do file I/O off the main thread (`Dispatchers.IO` + `viewModelScope.launch` or an interactor
coroutine scope) — reading/writing files synchronously on the UI thread is a normal Android
concern here, same as anywhere else.

### `SharedPreferences`

Obtain your plugin-scoped `SharedPreferences` instance through DI, keyed by your tool
descriptor's qualified name — this is what guarantees it's a preferences file private to
your tool rather than a name that could collide with another tool's:

```kotlin
internal val myToolModule = DI.Module("myToolModule") {
    bindSingleton {
        MyToolRepository(instance(arg = requireQualifiedName<MyToolDescriptor>()))
    }
}
```

Never call `Context.getSharedPreferences("my_prefs", MODE_PRIVATE)` directly with a
hand-picked name and assume isolation — go through DI with `requireQualifiedName<...>()` as
shown above and in [Data & domain](../architecture/data-and-domain.md).

---

## Cross-APK activation

If your APK wants to activate a tool that lives in a *different* installed plugin APK (not
one of your own descriptors), resolve it by fully-qualified descriptor name instead of a
reified `activate<T>()` call, since you don't have that class on your own classpath:

```kotlin
val toolId = toolManager.resolveToolId("com.other.plugin.TheirToolDescriptor")
toolId?.let { toolManager.activate(it, ToolManager.FLAG_COMPONENT_ON_TOP) }
```

`resolveToolId` returns `null` if the other APK isn't installed — disable or hide any UI
that depends on it in that case rather than assuming it will always resolve.

---

## Quick checklist for a new tool

- [ ] Read assets via `toolContext.assets`, not another `Context`.
- [ ] Localize every user-visible string (`values/` + every locale you support).
- [ ] Get `SharedPreferences` from DI via `requireQualifiedName<YourDescriptor>()`.
- [ ] Build any local database file path from `CommonSessionStorageInteractor.getUserDirectoryPath()`.
- [ ] Do file/database I/O off the main thread.
- [ ] If you use native code, set `useLegacyPackaging = true` for `jniLibs`.
- [ ] If you activate another APK's tool, use `resolveToolId` and handle `null`.
