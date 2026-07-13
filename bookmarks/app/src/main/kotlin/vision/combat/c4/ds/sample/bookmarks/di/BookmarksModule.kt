package vision.combat.c4.ds.sample.bookmarks.di

import android.content.Context
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf
import org.kodein.di.instance
import vision.combat.c4.ds.sample.bookmarks.data.BookmarkRepositoryImpl
import vision.combat.c4.ds.sample.bookmarks.data.db.BookmarkDatabase
import vision.combat.c4.ds.sample.bookmarks.data.db.dao.BookmarkDao
import vision.combat.c4.ds.sample.bookmarks.domain.interactor.BookmarkInteractor
import vision.combat.c4.ds.sample.bookmarks.domain.repository.BookmarkRepository
import vision.combat.c4.ds.sdk.domain.interactor.CommonSessionStorageInteractor

/**
 * Binds the domain-facing [BookmarkRepository] interface (from `:bookmarks:domain`) to its
 * `:bookmarks:data` implementation, [BookmarkRepositoryImpl] — the one place in the graph
 * that sees both types (the dependency-inversion seam). The [BookmarkDatabase] is built from an
 * isolated, user-scoped directory obtained via [CommonSessionStorageInteractor] (isolation
 * non-negotiable), mirroring the `storage` gallery sample's Room setup.
 */
internal val bookmarksModule = DI.Module("bookmarksModule") {
    // Room database stored under the user-scoped directory provided by the SDK.
    bindSingleton {
        val context = instance<Context>()
        val storageInteractor = instance<CommonSessionStorageInteractor>()
        BookmarkDatabase.getInstance(context, storageInteractor.getUserDirectoryPath())
    }

    // Expose the DAO so the repository can depend on it directly.
    bindSingleton { instance<BookmarkDatabase>().bookmarkDao() }

    bindSingleton<BookmarkRepository> {
        BookmarkRepositoryImpl(instance<BookmarkDao>())
    }
    bindSingletonOf(::BookmarkInteractor)
}
