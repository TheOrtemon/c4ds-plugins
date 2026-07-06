package vision.combat.c4.ds.sample.bookmarks.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import vision.combat.c4.ds.sample.bookmarks.domain.model.Bookmark
import vision.combat.c4.ds.sample.bookmarks.domain.repository.BookmarkRepository

/**
 * Thin use-case wrapping [BookmarkRepository]. Public so `:bookmarks:app` (a separate
 * module) can bind and depend on it.
 */
public class BookmarkInteractor(private val repository: BookmarkRepository) {
    public fun observeBookmarks(scope: CoroutineScope): StateFlow<List<Bookmark>> =
        repository.observeBookmarks(scope)

    public suspend fun addBookmark(label: String, target: String) {
        repository.add(label, target)
    }

    public suspend fun clearBookmarks() {
        repository.clear()
    }
}
