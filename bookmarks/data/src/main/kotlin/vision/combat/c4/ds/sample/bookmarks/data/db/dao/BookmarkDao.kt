package vision.combat.c4.ds.sample.bookmarks.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import vision.combat.c4.ds.sample.bookmarks.data.db.entity.BookmarkEntity

/**
 * Public so `:bookmarks:app` DI can bind it via `BookmarkDatabase.bookmarkDao()` — the DI seam
 * for this sample lives in the app module, unlike the single-module `storage` sample.
 */
@Dao
public interface BookmarkDao {

    @Query("SELECT * FROM bookmarks ORDER BY created_at DESC")
    public fun observeAll(): Flow<List<BookmarkEntity>>

    @Insert
    public suspend fun insert(entity: BookmarkEntity)

    @Query("DELETE FROM bookmarks")
    public suspend fun clearAll()
}
