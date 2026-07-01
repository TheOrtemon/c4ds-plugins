package vision.combat.c4.ds.sample.gallery.storage.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import vision.combat.c4.ds.sample.gallery.storage.data.db.dao.NoteDao
import vision.combat.c4.ds.sample.gallery.storage.data.db.entity.NoteEntity
import java.io.File

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
internal abstract class SampleDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var INSTANCE: SampleDatabase? = null

        /**
         * Returns the singleton [SampleDatabase] stored at an isolated path provided by the SDK
         * [vision.combat.c4.ds.sdk.domain.interactor.CommonSessionStorageInteractor].
         *
         * **Singleton assumption:** [INSTANCE] is initialised once with the [userDirectoryPath]
         * supplied on the *first* call. Subsequent calls return the cached instance regardless of
         * the path argument, because [CommonSessionStorageInteractor.getUserDirectoryPath] always
         * reflects the *current* user's directory and is expected to be stable for the lifetime of
         * this process. This is intentional for a simple sample; a production tool that supports
         * user or session switching would need to detect a directory change, close the old
         * instance, and rebuild it for the new path.
         *
         * @param context Application context (provided by the plugin host).
         * @param userDirectoryPath The user-scoped directory obtained from
         *   [vision.combat.c4.ds.sdk.domain.interactor.CommonSessionStorageInteractor.getUserDirectoryPath].
         */
        fun getInstance(context: Context, userDirectoryPath: String): SampleDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, userDirectoryPath).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context, userDirectoryPath: String): SampleDatabase {
            val dbFile = File(userDirectoryPath, DB_NAME)
            dbFile.parentFile?.mkdirs()
            return Room.databaseBuilder(
                context.applicationContext,
                SampleDatabase::class.java,
                dbFile.absolutePath,
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }

        private const val DB_NAME = "storage_sample.db"
    }
}
