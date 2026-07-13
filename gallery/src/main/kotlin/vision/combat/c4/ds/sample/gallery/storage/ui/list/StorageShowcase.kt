package vision.combat.c4.ds.sample.gallery.storage.ui.list

import androidx.annotation.Keep
import androidx.annotation.StringRes
import vision.combat.c4.ds.sample.gallery.R

/**
 * Static registry of the individual Storage showcases. Each entry becomes a row in the list
 * screen and navigates to its own dedicated detail screen.
 *
 * @param nameResId String resource for the showcase name (list title + detail title).
 * @param descResId String resource for the one-line showcase description.
 */
@Keep
internal enum class StorageShowcase(
    @get:StringRes val nameResId: Int,
    @get:StringRes val descResId: Int,
) {
    FILE(
        nameResId = R.string.storage_sc_file_name,
        descResId = R.string.storage_sc_file_desc,
    ),
    PREFERENCES(
        nameResId = R.string.storage_sc_prefs_name,
        descResId = R.string.storage_sc_prefs_desc,
    ),
    ROOM(
        nameResId = R.string.storage_sc_room_name,
        descResId = R.string.storage_sc_room_desc,
    ),
}
