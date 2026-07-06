package vision.combat.c4.ds.sample.bookmarks.data

import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import vision.combat.c4.ds.sample.bookmarks.domain.model.Bookmark
import vision.combat.c4.ds.sample.bookmarks.domain.repository.BookmarkRepository
import vision.combat.c4.ds.sdk.data.util.observeAsStateFlow

/**
 * `SharedPreferences`-backed implementation of [BookmarkRepository]. The [SharedPreferences]
 * instance is plugin-isolated — it is injected via Kodein with
 * `instance(arg = requireQualifiedName<BookmarksToolDescriptor>())`.
 *
 * The whole bookmark list is persisted as ONE JSON-array `String` under a single key and
 * observed via the SDK's `String` `observeAsStateFlow` path (the same path used by the
 * `storage` sample). A generic `Set<String>` observed through `observeAsStateFlow` does not
 * reliably emit updates, so bookmarks avoid that path entirely and encode the list as a single
 * JSON string instead.
 */
class BookmarkRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : BookmarkRepository {

    override fun add(label: String, target: String) {
        val current = sharedPreferences.getString(KEY_BOOKMARKS, "").orEmpty().toBookmarks()
        val newEntry = Bookmark(id = UUID.randomUUID().toString(), label = label, target = target)
        val updated = current + newEntry
        sharedPreferences.edit { putString(KEY_BOOKMARKS, updated.toJson()) }
    }

    override fun clear() {
        sharedPreferences.edit { remove(KEY_BOOKMARKS) }
    }

    override fun observeBookmarks(scope: CoroutineScope): StateFlow<List<Bookmark>> =
        sharedPreferences.observeAsStateFlow(KEY_BOOKMARKS, "", scope)
            .map { json -> json.toBookmarks() }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    private fun List<Bookmark>.toJson(): String {
        val array = JSONArray()
        forEach { bookmark ->
            array.put(
                JSONObject()
                    .put("id", bookmark.id)
                    .put("label", bookmark.label)
                    .put("target", bookmark.target),
            )
        }
        return array.toString()
    }

    private fun String.toBookmarks(): List<Bookmark> {
        if (isBlank()) return emptyList()
        val array = try {
            JSONArray(this)
        } catch (_: JSONException) {
            return emptyList()
        }
        return buildList {
            for (index in 0 until array.length()) {
                val entry = array.optJSONObject(index) ?: continue
                try {
                    add(
                        Bookmark(
                            id = entry.getString("id"),
                            label = entry.getString("label"),
                            target = entry.getString("target"),
                        ),
                    )
                } catch (_: JSONException) {
                    // Skip malformed entries; keep the rest of the list usable.
                }
            }
        }
    }

    private companion object {
        private const val KEY_BOOKMARKS = "bookmarks_json"
    }
}
