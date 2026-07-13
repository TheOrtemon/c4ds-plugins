package vision.combat.c4.ds.sample.gallery.mapview.mapinteractor.ui.list

import androidx.annotation.Keep
import androidx.annotation.StringRes
import vision.combat.c4.ds.sample.gallery.R

/**
 * Static registry of the individual CommonMapInteractor showcases. Each entry becomes a row in
 * the list screen and navigates to its own dedicated detail screen.
 *
 * @param nameResId String resource for the showcase name (list title + detail title).
 * @param descResId String resource for the one-line showcase description.
 */
@Keep
internal enum class MapInteractorShowcase(
    @get:StringRes val nameResId: Int,
    @get:StringRes val descResId: Int,
) {
    CAMERA_LOOKAT(
        nameResId = R.string.map_sc_camera_name,
        descResId = R.string.map_sc_camera_desc,
    ),
    FOCUS(
        nameResId = R.string.map_sc_focus_name,
        descResId = R.string.map_sc_focus_desc,
    ),
    DISPLAY_MODE(
        nameResId = R.string.map_sc_display_name,
        descResId = R.string.map_sc_display_desc,
    ),
    CURSOR(
        nameResId = R.string.map_sc_cursor_name,
        descResId = R.string.map_sc_cursor_desc,
    ),
    CORRECTIONS(
        nameResId = R.string.map_sc_corrections_name,
        descResId = R.string.map_sc_corrections_desc,
    ),
}
