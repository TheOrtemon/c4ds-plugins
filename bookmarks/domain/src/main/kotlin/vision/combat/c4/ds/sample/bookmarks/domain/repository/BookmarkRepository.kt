package vision.combat.c4.ds.sample.bookmarks.domain.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import vision.combat.c4.ds.sample.bookmarks.domain.model.Bookmark

/**
 * Domain-facing contract for the bookmarks sample. The concrete storage mechanism
 * (SharedPreferences, in `:bookmarks:data`) is a Data-layer implementation detail — Domain
 * only knows about this interface. Public because `:bookmarks:data` (impl) and
 * `:bookmarks:app` (DI binding) are separate Gradle modules.
 */
public interface BookmarkRepository {
    fun add(label: String, target: String)

    fun clear()

    fun observeBookmarks(scope: CoroutineScope): StateFlow<List<Bookmark>>
}
