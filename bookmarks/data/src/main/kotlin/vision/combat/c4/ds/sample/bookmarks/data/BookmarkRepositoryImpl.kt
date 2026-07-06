package vision.combat.c4.ds.sample.bookmarks.data

import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import vision.combat.c4.ds.sample.bookmarks.domain.model.Bookmark
import vision.combat.c4.ds.sample.bookmarks.domain.repository.BookmarkRepository
import vision.combat.c4.ds.sdk.data.util.observeAsStateFlow

/**
 * `SharedPreferences`-backed implementation of [BookmarkRepository]. The [SharedPreferences]
 * instance is plugin-isolated — it is injected via Kodein with
 * `instance(arg = requireQualifiedName<BookmarksToolDescriptor>())`.
 *
 * Bookmarks are persisted as a `Set<String>` of `id|label|target` entries under one key.
 */
public class BookmarkRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : BookmarkRepository {

    override fun add(label: String, target: String) {
        val entries = sharedPreferences.getStringSet(KEY_BOOKMARKS, emptySet()) ?: emptySet()
        val newEntry = Bookmark(id = UUID.randomUUID().toString(), label = label, target = target).serialize()
        sharedPreferences.edit { putStringSet(KEY_BOOKMARKS, entries + newEntry) }
    }

    override fun clear() {
        sharedPreferences.edit { putStringSet(KEY_BOOKMARKS, emptySet()) }
    }

    override fun observeBookmarks(scope: CoroutineScope): StateFlow<List<Bookmark>> =
        sharedPreferences.observeAsStateFlow(KEY_BOOKMARKS, emptySet<String>(), scope)
            .map { entries -> entries.mapNotNull { entry -> entry.toBookmark() } }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    private fun Bookmark.serialize(): String = listOf(id, label, target).joinToString(DELIMITER)

    private fun String.toBookmark(): Bookmark? {
        val parts = split(DELIMITER)
        if (parts.size != 3) return null
        return Bookmark(id = parts[0], label = parts[1], target = parts[2])
    }

    private companion object {
        private const val KEY_BOOKMARKS = "bookmarks"
        private const val DELIMITER = "|"
    }
}
