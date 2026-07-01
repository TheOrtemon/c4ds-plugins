package vision.combat.c4.ds.sample.gallery.storage.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import vision.combat.c4.ds.sample.gallery.storage.data.db.entity.NoteEntity

@Dao
internal interface NoteDao {

    @Insert
    suspend fun insert(note: NoteEntity)

    @Query("SELECT * FROM notes ORDER BY created_at DESC")
    suspend fun getAll(): List<NoteEntity>

    @Query("DELETE FROM notes")
    suspend fun clearAll()
}
