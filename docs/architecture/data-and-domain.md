# Data & domain

**[← AGENTS.md](../../AGENTS.md)**

Repository / interactor / mapper patterns for a tool's data and domain layers, and how to
use the SDK's isolated storage primitives (`SharedPreferences`, files, Room).

> **Sibling docs:** [Architecture for plugins](architecture-for-plugins.md) ·
> [UI layer conventions](ui-layer-conventions.md) · [Resource & isolation](../reference/resource-and-isolation.md) ·
> [Testing your tool](../testing/testing-your-tool.md)

---

## Repository — owns one storage concern

A repository wraps exactly one storage mechanism (a `SharedPreferences` instance, a Room
DAO, a directory of files) and exposes a small, purpose-built API — never the raw storage
type itself. Domain declares the repository as an **interface**; Data provides the
`...Impl` that implements it — this is the dependency inversion pattern applied concretely:

```kotlin
// domain/repository/MyToolRepository.kt
internal interface MyToolRepository {
    fun setShowDescription(show: Boolean)

    fun observeShowDescription(scope: CoroutineScope): StateFlow<Boolean>
}
```

```kotlin
// data/MyToolRepositoryImpl.kt
internal class MyToolRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : MyToolRepository {
    override fun setShowDescription(show: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_SHOW_DESCRIPTION, show) }
    }

    override fun observeShowDescription(scope: CoroutineScope): StateFlow<Boolean> =
        sharedPreferences.observeAsStateFlow(KEY_SHOW_DESCRIPTION, true, scope)

    private companion object {
        private const val KEY_SHOW_DESCRIPTION = "show_description"
    }
}
```

- Keys are `private const val`s local to the impl — nothing outside it should know the
  underlying storage shape. The interface only exposes domain-facing methods, never the key
  names or the `SharedPreferences` type itself.
- Expose `StateFlow`/`Flow` for reads, plain functions for writes. `observeAsStateFlow` (an
  SDK extension on `SharedPreferences`) turns a preference key into a `StateFlow` you can
  collect like any other reactive source.
- The `SharedPreferences` instance itself comes from DI, isolated to your tool — see
  [Resource & isolation](../reference/resource-and-isolation.md) for how it's obtained and why you should
  never substitute an ambient, hardcoded-name `SharedPreferences` instead.
- The interface lives in `domain/repository/`; the impl lives in `data/`. The DI module
  binds the interface to the impl (see below), so everything above Domain — interactors,
  ViewModels — depends only on the interface.

### Room

For structured local data, wrap a Room database the same way — one repository (or DAO
injected directly, for very simple cases) per feature:

```kotlin
@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
internal abstract class MyToolDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile private var INSTANCE: MyToolDatabase? = null

        fun getInstance(context: Context, userDirectoryPath: String): MyToolDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, userDirectoryPath).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context, userDirectoryPath: String): MyToolDatabase {
            val dbFile = File(userDirectoryPath, "my_tool.db")
            dbFile.parentFile?.mkdirs()
            return Room.databaseBuilder(context.applicationContext, MyToolDatabase::class.java, dbFile.absolutePath)
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }
    }
}
```

- Build the database file inside the **user-scoped directory** the SDK's session-storage
  interactor gives you (`userDirectoryPath` above) — not an arbitrary app-private path — so
  the database is correctly isolated per user/session. See
  [Resource & isolation](../reference/resource-and-isolation.md#session-scoped-storage).
- A singleton guarded by `synchronized` is the standard shape; note that it's initialized
  once from whatever path is passed on the *first* call. If your tool needs to support
  switching users/sessions within one process lifetime, you'll need to detect a directory
  change and rebuild the instance — a simple tool usually doesn't need this.
- Bind the DAO (not the database class) into your DI module so ViewModels/repositories that
  need only queries don't have to know the database exists:

```kotlin
internal val myToolModule = DI.Module("myToolModule") {
    bindSingleton {
        val context = instance<Context>()
        val storageInteractor = instance<CommonSessionStorageInteractor>()
        MyToolDatabase.getInstance(context, storageInteractor.getUserDirectoryPath())
    }
    bindSingleton { instance<MyToolDatabase>().noteDao() }
}
```

### Remote data — Ktor

For remote data, wrap a Ktor service the same way: the repository interface stays
storage-agnostic, and the impl owns the transport plus the DTO → domain mapping. The host
already ships the full Ktor client stack (core, Android engine, ContentNegotiation,
kotlinx-serialization json) and the SDK exposes it via `api`, so `compileOnly(c4ds-sdk)`
gives you every class at compile time and the host provides them at runtime — declare
**no** Ktor dependency of your own.

```kotlin
internal val myToolModule = DI.Module("myToolModule") {
    // The SDK deliberately leaves the untagged HttpClient slot free (its shared client is
    // bound under SdkRemoteTags.HTTP_CLIENT), so a tool can bind its own configured client
    // without a Kodein OverridingException.
    bindSingleton {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    // HttpClient creation is heavy; inject a provider so the service resolves it lazily on
    // the first request.
    bindSingleton { MyApiService(provider()) }
}
```

- Prefer binding your **own** untagged `HttpClient` configured for your API. If the host's
  default configuration suits you, resolve the shared one instead:
  `instance<HttpClient>(tag = SdkRemoteTags.HTTP_CLIENT)`.
- Keep the service pure transport (build request, deserialize response) and map DTOs to
  domain models at the repository boundary, mirroring the storage patterns above.
- Run requests off the main thread (`withContext(Dispatchers.IO)` in the repository impl).

The runnable version of this pattern is the **Network Requests** sample —
[`gallery/.../network/`](../../gallery/src/main/kotlin/vision/combat/c4/ds/sample/gallery/network)
(see [Samples guidebook — Section 10](../guides/samples-catalog.md#section-10-data-management)).

---

## Interactor — the domain layer

An interactor (use case) sits between the UI layer and one or more repositories/SDK domain
interactors. It's where you put logic that isn't pure storage plumbing and isn't UI
rendering:

```kotlin
internal class MyToolInteractor(
    private val repository: MyToolRepository,
) {
    fun observeShowDescription(scope: CoroutineScope): StateFlow<Boolean> =
        repository.observeShowDescription(scope)

    fun setShowDescription(show: Boolean) {
        repository.setShowDescription(show)
    }
}
```

For a trivial pass-through tool this can look like it's "just forwarding" — that's fine. The
value of the interactor boundary shows up once a tool needs to:

- Combine more than one repository or SDK interactor into a single use case.
- Apply domain rules (validation, derived state, mapping between a persisted shape and a
  domain model) that shouldn't leak into the UI layer.
- Stay swappable/testable independent of the concrete storage mechanism underneath.

### Composing with SDK domain interactors

Real tools frequently combine their own repository with an SDK-provided domain interactor
(map state, model/battlespace state, locale settings, session storage). Keep that
composition in the domain layer, not the ViewModel:

```kotlin
internal class MyToolInteractor(
    private val repository: MyToolRepository,
    private val mapInteractor: CommonMapInteractor,
) {
    fun currentSelectedPoint() = mapInteractor.selectedPosition.value

    fun persistLastSelection() {
        repository.setLastSelection(currentSelectedPoint())
    }
}
```

This keeps the ViewModel a thin adapter between `Action`/`UiState` and the domain layer,
which is what makes ViewModel tests fast and mock-friendly (see
[Testing your tool](../testing/testing-your-tool.md)).

---

## Mappers

When a repository's storage shape (an `Entity`, a raw `SharedPreferences` key/value pair)
differs from the domain/UI-facing model, keep the conversion as a small pure function —
either a top-level extension (`NoteEntity.toDomain()`) or a dedicated `Mapper` class if
there's meaningful logic on both directions. Pure functions like this need no mocks to test;
see [Testing your tool — mappers](../testing/testing-your-tool.md#what-to-test-and-what-not-to).

---

## Dependency direction, restated

The chain above (`ViewModel → Interactor → Repository → SharedPreferences / Room / files /
SDK interactor`) is the **runtime call flow** — the order in which one layer actually calls
into the next when the app is running. It is not the same thing as **compile-time
dependency**, which points the other way for Domain and Data:

```
UI (ViewModel)  ──depends on──>  Domain (Interactor + repository interface)  <──depends on──  Data (Repository impl)
```

Domain declares the repository as an **interface** (and the domain models it works with);
Data depends on Domain by providing the concrete implementation of that interface — this is
dependency inversion (DIP). Domain itself has no compile-time dependency on
`SharedPreferences`, Room, or any other storage type; only the Data-layer implementation
does. See [Architecture for plugins — the layer cake](architecture-for-plugins.md#the-layer-cake)
for the full picture.

The DI module is where the interface gets bound to its impl — this is the one place that
knows both types exist:

```kotlin
internal val myToolModule = DI.Module("myToolModule") {
    bindSingleton<MyToolRepository> {
        MyToolRepositoryImpl(instance(arg = requireQualifiedName<MyToolDescriptor>()))
    }
    bindSingletonOf(::MyToolInteractor)
}
```

`bindSingleton<MyToolRepository> { ... }` binds against the interface type, so `instance()`
resolution anywhere else in the graph (including inside `MyToolInteractor`'s constructor)
returns `MyToolRepository`, never `MyToolRepositoryImpl`.

A ViewModel should never import `android.content.SharedPreferences`, `androidx.room.*`, or
`java.io.File` directly. If you find yourself doing that, the storage access belongs one
layer down.
