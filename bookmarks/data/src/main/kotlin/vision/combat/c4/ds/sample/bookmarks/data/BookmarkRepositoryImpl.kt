package vision.combat.c4.ds.sample.bookmarks.data

import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import vision.combat.c4.ds.sample.bookmarks.data.db.dao.BookmarkDao
import vision.combat.c4.ds.sample.bookmarks.data.db.entity.BookmarkEntity
import vision.combat.c4.ds.sample.bookmarks.data.db.entity.toDomain
import vision.combat.c4.ds.sample.bookmarks.data.db.entity.toEntity
import vision.combat.c4.ds.sample.bookmarks.domain.model.Bookmark
import vision.combat.c4.ds.sample.bookmarks.domain.repository.BookmarkRepository

/**
 * Room-backed implementation of [BookmarkRepository]. The [BookmarkDao] is bound to an isolated
 * [vision.combat.c4.ds.sample.bookmarks.data.db.BookmarkDatabase] stored under the SDK's
 * user-scoped directory (see `:bookmarks:app`'s `di/BookmarksModule.kt`), mirroring the `storage`
 * gallery sample's Room setup.
 */
class BookmarkRepositoryImpl(
    private val dao: BookmarkDao,
) : BookmarkRepository {

    override suspend fun add(label: String, target: String) {
        val newEntry = Bookmark(id = UUID.randomUUID().toString(), label = label, target = target)
        dao.insert(newEntry.toEntity())
    }

    override suspend fun clear() {
        dao.clearAll()
    }

    override fun observeBookmarks(scope: CoroutineScope): StateFlow<List<Bookmark>> =
        dao.observeAll()
            .map { entities -> entities.map(BookmarkEntity::toDomain) }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())
}
