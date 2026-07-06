# Review rules

**[← AGENTS.md](../AGENTS.md)**

A generic checklist for reviewing an external c4ds tool's code. Use it to check that a
change follows the conventions described elsewhere in `docs/` — it doesn't introduce new
rules, just collects the review-relevant ones in one place.

---

## Architecture / MVI / DI

- **Layering points one direction.** UI depends on Domain; Data depends on Domain by
  implementing its repository interfaces (dependency inversion). Domain has no compile-time
  dependency on UI or Data. See
  [Architecture for plugins — the layer cake](architecture/architecture-for-plugins.md#the-layer-cake).
- **Composables and ViewModels never touch storage directly** (`SharedPreferences`, Room,
  `File`) — they go through a repository via an interactor.
- **A ViewModel's public surface is exactly**: one `StateFlow<UiState>`, one sealed `Action`
  entry point, one event `Flow` for one-shot side effects. See
  [UI layer conventions — MVI shape](architecture/ui-layer-conventions.md#mvi-shape).
- **DI is Kodein**, scoped per tool via `subDI(super.di) { import(yourModule) }` on
  `AbstractTool`/`AbstractToolService`; ViewModels resolve through `diViewModel()`, not a
  hand-rolled `ViewModelProvider.Factory`. See
  [Tool lifecycle & setup — Kodein DI lifecycle](reference/tool-lifecycle-and-setup.md#kodein-di-lifecycle).
- **`AbstractTool` and `ToolDescriptor` live in separate files** (e.g. `MyTool.kt` /
  `MyToolDescriptor.kt`), matching every sample in this repo.

## Threading / lifecycle / Compose

- **No I/O on the main thread.** File, database, and network access happen off
  `Dispatchers.Main`, typically inside `viewModelScope.launch(Dispatchers.IO) { }` or an
  interactor's own coroutine scope.
- **Long-lived work uses `viewModelScope`** (or a service's own `coroutineScope`), not a
  scope that outlives or is disconnected from the owning lifecycle.
- **Side effects belong in `LaunchedEffect`/`DisposableEffect`, never in the composable
  body.** Don't collect a `Flow` or call an interactor directly while composing — do it in
  the ViewModel and expose `UiState`, or in a keyed `LaunchedEffect` for event handling. See
  [UI layer conventions — what not to do](architecture/ui-layer-conventions.md#what-not-to-do).
- **Lambdas passed into composables should be stable** — hoist them from a `remember` or
  pass method references / stable fields rather than allocating a fresh lambda every
  recomposition where it would defeat skipping.

## Resources / locale

- **No hardcoded UI strings.** Every user-visible string comes from `values/strings.xml` (or
  another resource type), never a literal in Kotlin/Compose code.
- **Ship every supported locale.** If the tool supports more than one language, add the
  matching `values-<locale>/` resource set for each string added or changed — don't leave a
  locale partially translated. See
  [Resource & isolation — resources are yours, config-reactive](reference/resource-and-isolation.md#resources-are-yours-config-reactive).

## Testing

- **ViewModels, interactors, and mappers are covered by unit tests.** Composables are not
  unit-tested (verify those visually / with `@Preview`). See
  [Testing your tool — what to test](testing/testing-your-tool.md#what-to-test-and-what-not-to).
- **Toolchain is JUnit 5 + MockK + Turbine**, on the plain JVM (no emulator/instrumentation
  test needed if layering is respected). See
  [Testing your tool — toolchain](testing/testing-your-tool.md#toolchain).
- A repository is worth its own test once it has real branching (caching, fallback,
  combining sources) — a pure pass-through repository usually doesn't need one.

## Context validation

Any Compose construct that opens a **new window or sub-composition** (a raw `Dialog`,
`Popup`, `DropdownMenu`, a bottom sheet, …) resets `LocalContext` back to the host inside
that sub-composition. If plugin code calls `stringResource()`, `painterResource()`, or
otherwise reads assets/resources from inside one of these without re-providing the tool's
context, it silently resolves against the host instead of the plugin — missing resources or,
worse, a resource ID that happens to collide with an unrelated host resource. See
[Resource & isolation — Material widgets across the window boundary](reference/resource-and-isolation.md#material-widgets-across-the-window-boundary)
for the underlying mechanism.

Checklist:

- **Raw `Dialog`, `AlertDialog`, `Popup`, `DropdownMenu`, `ExposedDropdownMenu` in tool
  code.** Either use an SDK wrapper that handles context for you (e.g. `ToolAlertDialog`, or
  `ToolDialog` via `AbstractTool.showDialog`), or capture the current context with
  `LocalContext.current` *before* opening the popup and re-provide it inside the popup's
  content using the SDK helper that re-provides the window context (`ProvideWindowContext`).
- **A nested popup/dialog opened inside an already-wrapped dialog's content.** Capture and
  re-provide the context again at that inner boundary — the outer wrapper only covers its own
  sub-composition, not popups opened from within it.
- **Bottom sheets.** Sheet content is not auto-wrapped by the sheet itself; wrap it the same
  way as a raw dialog/popup if it resolves any plugin resource.
- **Snackbars, and any popup that renders in the main composition** (i.e. it isn't hosted in
  a separate window/sub-composition). `LocalContext` doesn't reset there, so no re-provisioning
  is needed — flag wrapping added "just in case" here as unnecessary noise, not as defense in
  depth.
- **`AndroidView` factory.** Pass the composition's current context to the `View` you create;
  if that `View` later opens its own `Dialog` or shows a `Toast`, pass it that same captured
  context rather than whatever context the `View` framework hands it by default.
- **Imperative `Dialog`/`Toast` calls from non-Compose code.** Pass the tool's context
  explicitly — never the host context.
- **Stale context.** Don't cache a captured `context` in a `remember { }` block or a class
  field and reuse it later. Read the current context at the call site, inside the body of the
  coroutine/`LaunchedEffect`/callback that needs it, so it stays correct across locale/context
  changes instead of pinning a context captured at an earlier point in time.

## Dependency hygiene

- **Version bumps are deliberate**, not incidental. A dependency version (SDK, Kotlin,
  AndroidX, or any third-party library) changes only when the PR is specifically about that
  upgrade, with the reasoning in the PR description — not as a side effect of an unrelated
  change touching a version catalog file.
- Prefer resolving new dependency versions from the project's own version catalog /
  package registry over copying a version number from an unrelated source.

### Gradle dependency configuration

- **The SDK is `compileOnly` + `runtimeOnly`, never `implementation`/`api`.** You compile
  against the SDK's API surface, but the host provides the real classes at runtime, so the
  SDK artifact belongs on `compileOnly`; the small runtime shim it needs at activation time
  goes on `runtimeOnly`. Flag any tool that declares the SDK via `implementation` or `api` —
  that bundles host-owned classes into the tool's own APK and causes duplicate-class, DEX, or
  version-skew problems against what the host actually loads at runtime. See
  [Getting started — Module dependencies](guides/getting-started.md#3-module-dependencies)
  for the exact block to compare against.
- **`runtimeOnly` excludes what the host already ships**, most commonly the Kotlin stdlib
  (`exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")` on the `runtimeOnly`
  configuration). Check that an equivalent exclude is present whenever the runtime artifact
  would otherwise pull in a host-provided transitive dependency a second time.
- **Don't re-declare host-provided libraries via `implementation`/`api`.** Compose, the Kotlin
  stdlib, Coroutines, AndroidX Core/Lifecycle, Material, Room, and the SDK's own transitive
  dependencies are already on the host's runtime classpath — the SDK exposes them via `api`,
  so `compileOnly(c4ds-sdk)` gives you them at compile time (for Room, add your own
  `ksp(androidx.room.compiler)` for codegen; the runtime comes from the host). Adding them
  again as `implementation`/`api` duplicates classes in the APK and risks runtime conflicts —
  use `compileOnly` for these instead. Reserve `implementation`/`api` for dependencies that
  are genuinely unique to the tool and not already provided by the host or SDK.
- **Check the resolved release runtime classpath, not just the declared dependency lines.**
  A host-provided library can leak into the APK transitively through an unrelated
  third-party `implementation` dependency even when no line names it directly. Declared-line
  review alone won't catch this; run
  `./gradlew :yourModule:dependencies --configuration releaseRuntimeClasspath` and inspect
  the resolved tree. Flag any host-provided library (coroutines, Compose, AndroidX, Room, etc.)
  that shows up there and require it excluded on the dependency that pulls it in (e.g.
  `exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-android")` on that
  dependency), not stripped globally via a blanket `configurations.all { exclude(...) }`
  — that would also remove the SDK's own `compileOnly` copy and break compilation. Left
  unexcluded, R8's `-repackageclasses` renames the bundled copy into the plugin's own
  package, and it no longer matches the host's identically-named classes at runtime —
  `IncompatibleClassChangeError`. See
  [Getting started — Release builds and obfuscation](guides/getting-started.md#release-builds-and-obfuscation)
  for the full example.
- **Prefer version-catalog aliases** (`libs.combat.ds.sdk`, `libs.combat.ds.sdk.runtime`, …)
  over hardcoded coordinates, and bump versions deliberately per the rule above.
- **Release builds set a unique `-repackageclasses`.** For any build type with
  `isMinifyEnabled = true`, the module's `proguard-rules.pro` must include
  `-repackageclasses <applicationId>.obf`, with `<applicationId>` matching that module's own
  `applicationId` (never copied from another app). Without it, R8's obfuscated class names can
  collide with the host's or another plugin's identically-named obfuscated classes at runtime,
  which silently breaks the tool. See
  [Getting started — Release builds and obfuscation](guides/getting-started.md#release-builds-and-obfuscation)
  for the full explanation.
- **Flag a blanket `-keep class <pkg>.** { *; }` in `proguard-rules.pro` as an anti-pattern.**
  Descriptor, ViewModel, and `R$*` keeps are already inherited from the SDK's consumer
  ProGuard rules — a plugin never needs to `-keep` its own package wholesale. The only
  required custom rule is `-repackageclasses` (above); a blanket keep just disables shrinking
  and obfuscation for the whole module and should be replaced with the minimal rule set. See
  [Getting started — Release builds and obfuscation](guides/getting-started.md#release-builds-and-obfuscation).

---

**See also:** [Architecture for plugins](architecture/architecture-for-plugins.md) ·
[UI layer conventions](architecture/ui-layer-conventions.md) ·
[Data & domain](architecture/data-and-domain.md) ·
[Tool lifecycle & setup](reference/tool-lifecycle-and-setup.md) ·
[Resource & isolation](reference/resource-and-isolation.md) ·
[Testing your tool](testing/testing-your-tool.md)
