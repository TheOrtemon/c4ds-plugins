package vision.combat.c4.ds.sample.bookmarks.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import vision.combat.c4.ds.sample.bookmarks.data.db.dao.BookmarkDao
import vision.combat.c4.ds.sample.bookmarks.data.db.entity.BookmarkEntity
import java.io.File

/**
 * Public so `:bookmarks:app` DI can build the instance and bind [bookmarkDao] — the DI seam for
 * this sample lives in the app module, unlike the single-module `storage` sample where the
 * equivalent `SampleDatabase` is internal.
 */
@Database(entities = [BookmarkEntity::class], version = 1, exportSchema = false)
public abstract class BookmarkDatabase : RoomDatabase() {

    public abstract fun bookmarkDao(): BookmarkDao

    public companion object {

        @Volatile
        private var INSTANCE: BookmarkDatabase? = null

        /**
         * Returns the singleton [BookmarkDatabase] stored at an isolated path provided by the SDK
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
        public fun getInstance(context: Context, userDirectoryPath: String): BookmarkDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, userDirectoryPath).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context, userDirectoryPath: String): BookmarkDatabase {
            val dbFile = File(userDirectoryPath, DB_NAME)
            dbFile.parentFile?.mkdirs()
            return Room.databaseBuilder(
                context.applicationContext,
                BookmarkDatabase::class.java,
                dbFile.absolutePath,
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }

        private const val DB_NAME = "bookmarks.db"
    }
}
