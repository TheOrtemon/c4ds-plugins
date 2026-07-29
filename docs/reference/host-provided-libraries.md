# Host-provided libraries

**[← AGENTS.md](../../AGENTS.md)**

`compileOnly(c4ds-sdk)` does more than give you SDK types: the SDK re-exports a large slice of its
own dependency graph with `api(...)`, and the host ships every one of those libraries in its APK.
So you can `import` and use them directly — no `implementation` line, nothing added to your APK.

This doc is the **catalog**: what is on that list, at which version. The *rules* around it live
elsewhere — see [Getting started — Module dependencies](../guides/getting-started.md#3-module-dependencies)
for the dependency block, [Getting started — Release builds and obfuscation](../guides/getting-started.md#release-builds-and-obfuscation)
for why bundling a host-provided library breaks R8 builds, and
[Review rules — Gradle dependency configuration](../review-rules.md#gradle-dependency-configuration)
for what a reviewer checks.

> **Versions below are the ones `c4ds-sdk 0.5.1` resolves** (the version this repo pins in
> [`gradle/libs.versions.toml`](../../gradle/libs.versions.toml)). They move with the SDK — regenerate
> the list for your own SDK version with the command in [Checking it yourself](#checking-it-yourself)
> rather than copying these numbers into your build.

---

## The rule

| You want to use…                                    | Declare                                                        |
|-----------------------------------------------------|----------------------------------------------------------------|
| Anything in the tables below                        | **nothing** — it arrives via `compileOnly(c4ds-sdk)`            |
| An annotation processor for one of them (e.g. Room) | `ksp(...)` only — the runtime still comes from the host         |
| OkHttp                                              | `compileOnly(...)` at the host's exact version — [why](#present-in-the-host-but-not-on-the-api-graph) |
| A library genuinely not on the list                 | `implementation(...)`, and read the R8/keep-rules notes first   |

Adding a host-provided library as `implementation` puts a second copy of its classes in your APK.
In a release build R8's `-repackageclasses` then renames *your* copy, and at runtime you get two
incompatible versions of the same class loaded through two classloaders.

---

## Kotlin and kotlinx

| Library                                       | Version  |
|-----------------------------------------------|----------|
| `org.jetbrains.kotlin:kotlin-stdlib`          | 2.4.10   |
| `org.jetbrains.kotlin:kotlin-reflect`         | 2.4.10   |
| `org.jetbrains.kotlinx:kotlinx-coroutines-core` | 1.11.0 |
| `org.jetbrains.kotlinx:kotlinx-serialization-json` | 1.11.0 |
| `org.jetbrains.kotlinx:kotlinx-serialization-protobuf` | 1.11.0 |
| `org.jetbrains.kotlinx:kotlinx-datetime`      | 0.8.0    |

## Compose and Compose-adjacent AndroidX

The host is on **Material 2** (`androidx.compose.material`). Material 3 is *not* provided — see
[What is not provided](#what-is-not-provided).

| Library                                          | Version |
|--------------------------------------------------|---------|
| `androidx.compose.runtime:runtime`               | 1.11.4  |
| `androidx.compose.ui:ui`                         | 1.11.4  |
| `androidx.compose.ui:ui-util`                    | 1.11.4  |
| `androidx.compose.ui:ui-tooling-preview`         | 1.11.4  |
| `androidx.compose.foundation:foundation`         | 1.11.4  |
| `androidx.compose.material:material`             | 1.11.4  |
| `androidx.compose.material:material-icons-extended` | 1.7.8 |
| `androidx.activity:activity-compose`             | 1.13.0  |
| `androidx.navigation:navigation-compose`         | 2.9.8   |
| `androidx.lifecycle:lifecycle-viewmodel`         | 2.11.0  |
| `androidx.lifecycle:lifecycle-viewmodel-compose` | 2.11.0  |
| `androidx.lifecycle:lifecycle-runtime-compose`   | 2.11.0  |
| `androidx.lifecycle:lifecycle-runtime-ktx`       | 2.11.0  |
| `com.google.accompanist:accompanist-permissions` | 0.37.3  |

## Other AndroidX

| Library                       | Version | Note                                                        |
|-------------------------------|---------|-------------------------------------------------------------|
| `androidx.core:core`          | 1.19.0  | `core-ktx` 1.18.0 comes along transitively                   |
| `androidx.appcompat:appcompat`| 1.7.1   |                                                             |
| `androidx.room:room-runtime`  | 2.8.4   | runtime only — add your own `ksp(androidx.room.compiler)`    |
| `androidx.room:room-ktx`      | 2.8.4   |                                                             |

## Dependency injection

| Library                                            | Version |
|----------------------------------------------------|---------|
| `org.kodein.di:kodein-di`                          | 7.32.0  |
| `org.kodein.di:kodein-di-framework-android-x-compose` | 7.32.0 |

## Networking and serialization

Enough Ktor for a full client — see the **Network Requests** sample in the
[samples guidebook](../guides/samples-catalog.md#section-10-data-management), which builds its own
`HttpClient` out of these without adding a single dependency.

| Library                                  | Version |
|------------------------------------------|---------|
| `io.ktor:ktor-client-core`               | 3.5.1   |
| `io.ktor:ktor-client-android`            | 3.5.1   |
| `io.ktor:ktor-client-content-negotiation`| 3.5.1   |
| `io.ktor:ktor-client-logging`            | 3.5.1   |
| `io.ktor:ktor-serialization-kotlinx-json`| 3.5.1   |

## Images and media

| Library                        | Version |
|--------------------------------|---------|
| `io.coil-kt.coil3:coil-compose`| 3.5.0   |
| `io.coil-kt.coil3:coil-core`   | 3.5.0   |
| `io.coil-kt.coil3:coil-video`  | 3.5.0   |

## Map, model, and military symbology

| Library                                       | Version  |
|-----------------------------------------------|----------|
| `earth.worldwind:worldwind`                   | 2.0.7    |
| `vision.combat:c4model`                       | 1.15.8   |
| `vision.combat:c4unit`                        | 1.15.8   |
| `vision.combat:c4view-symbol`                 | 1.15.8   |
| `io.github.missioncommand:mil-sym-android`    | 2.11.2   |
| `io.matthewnelson.kmp-file:file`              | 0.6.1    |

---

## The declared surface vs. the whole classpath

The tables above are the SDK's **declared** `api` surface — the part treated as a contract. Their
own transitive dependencies are on your compile classpath too (≈230 modules in total for 0.5.1):
`androidx.annotation` 1.10.0, `androidx.collection` 1.5.0, `androidx.sqlite` 2.6.2,
`org.jetbrains.kotlinx:kotlinx-io-core` 0.9.0, `com.squareup.okio:okio` 3.17.0, and so on.

They compile — but **several of them do not survive to runtime**. The host is minified with
`-repackageclasses`, and only the declared surface is kept by name. Measured against a release host
build, these are renamed to `vision.combat.c4.ds.app.obf.*` and will throw `ClassNotFoundException`
if your plugin names them: `okio.**`, `com.caverock.androidsvg.**`,
`dev.icerock.moko.resources.**`, `app.softwork.uuid.**`.

Debug builds are not minified, so this class of bug **only appears in release** — which is exactly
when it is most expensive to find. Note the trap in `okio`'s case: parts of Coil's own API surface
(`DiskCache`, `ImageSource`) take okio types, so a plugin can reach an obfuscated class through a
kept one.

Beyond that, a deeper transitive can change version or disappear entirely in the next SDK release
without that being a breaking change. Stick to the declared surface; if you lean on something
deeper, pin the SDK version you tested against **and verify against a minified host**, not just a
debug one.

## Present in the host, but not on the api graph

One library is deliberately host-provided **without** being re-exported: **OkHttp**.

| Library                        | Version |
|--------------------------------|---------|
| `com.squareup.okhttp3:okhttp`  | 5.4.0   |

It reaches the host APK transitively and the SDK's consumer ProGuard rules keep `okhttp3.**` by
name specifically so plugins can resolve it parent-first at runtime — but nothing in the SDK's `api`
graph names it, so it is not on your compile classpath. If you need it, declare it **`compileOnly`
at exactly the host's version**:

```kotlin
compileOnly("com.squareup.okhttp3:okhttp:5.4.0")
```

`implementation` would bundle a second copy and R8 would repackage it, which is the exact failure
the host's keep rule exists to prevent. The host pins this version strictly, so treat a mismatch as
a build error waiting to happen rather than something conflict resolution will sort out.

## What is not provided

Common libraries that are **not** on the classpath, and that you must declare yourself if you need
them (with the bundling caveats above):

- **Compose Material 3** (`androidx.compose.material3`) — the host UI and every SDK component is
  Material 2. Mixing an M3 copy of your own into host-composed surfaces is asking for theme and
  popup-context surprises; see [Resource & isolation — Material widgets across the window boundary](resource-and-isolation.md#material-widgets-across-the-window-boundary).
- **`androidx.room:room-compiler`** — a build-time KSP processor, never a runtime class. Declare
  `ksp(androidx.room.compiler)` yourself; the runtime stays host-provided.
- **`com.android.tools:desugar_jdk_libs`** — declare `coreLibraryDesugaring(...)` in your own module.
- **Retrofit / Moshi / Gson** — Ktor + kotlinx-serialization are the provided stack. (OkHttp is a
  special case — see [Present in the host, but not on the api graph](#present-in-the-host-but-not-on-the-api-graph).)
- **DataStore, WorkManager, CameraX**, and most other AndroidX artifacts outside the tables above.
- **Test libraries** (JUnit 5, MockK, Turbine) — your own `testImplementation`, see
  [Testing your tool](../testing/testing-your-tool.md).

## Checking it yourself

The list above is generated from a resolved classpath, not hand-maintained. To regenerate it for
your SDK version, resolve any module that depends on the SDK:

```bash
./gradlew :gallery:dependencies --configuration debugCompileClasspath
```

Everything under the `vision.combat:c4ds-sdk` node is host-provided. To see only the SDK's declared
surface (the tables above), read the `api` variants of the SDK's Gradle module metadata:

```bash
find ~/.gradle/caches/modules-2/files-2.1/vision.combat -name 'c4ds-sdk*-*.module'
```

## Quick checklist

- [ ] Nothing from the tables above appears in your `dependencies` block as `implementation`/`api`.
- [ ] Room users declare `ksp(androidx.room.compiler)` and nothing else Room-related.
- [ ] Any library you *do* bundle is genuinely absent from the resolved SDK classpath.
- [ ] You checked the resolved release runtime classpath, not just the declared lines
      ([Review rules](../review-rules.md#gradle-dependency-configuration)).
