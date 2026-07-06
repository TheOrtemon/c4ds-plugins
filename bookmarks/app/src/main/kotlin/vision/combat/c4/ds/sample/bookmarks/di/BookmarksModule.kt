package vision.combat.c4.ds.sample.bookmarks.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf
import org.kodein.di.instance
import vision.combat.c4.ds.sample.bookmarks.BookmarksToolDescriptor
import vision.combat.c4.ds.sample.bookmarks.data.BookmarkRepositoryImpl
import vision.combat.c4.ds.sample.bookmarks.domain.interactor.BookmarkInteractor
import vision.combat.c4.ds.sample.bookmarks.domain.repository.BookmarkRepository
import vision.combat.c4.ds.sdk.tool.requireQualifiedName

/**
 * Binds the domain-facing [BookmarkRepository] interface (from `:bookmarks:domain`) to its
 * `:bookmarks:data` implementation, [BookmarkRepositoryImpl] — the one place in the graph
 * that sees both types (the dependency-inversion seam). `instance(arg = ...)` supplies the
 * tool-scoped [android.content.SharedPreferences] (isolation non-negotiable).
 */
internal val bookmarksModule = DI.Module("bookmarksModule") {
    bindSingleton<BookmarkRepository> {
        BookmarkRepositoryImpl(instance(arg = requireQualifiedName<BookmarksToolDescriptor>()))
    }
    bindSingletonOf(::BookmarkInteractor)
}
