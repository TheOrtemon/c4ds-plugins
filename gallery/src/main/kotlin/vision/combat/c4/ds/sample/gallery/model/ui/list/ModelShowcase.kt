package vision.combat.c4.ds.sample.gallery.model.ui.list

import androidx.annotation.Keep
import androidx.annotation.StringRes
import vision.combat.c4.ds.sample.gallery.R

/**
 * Static registry of the individual CommonModelInteractor showcases. Each entry becomes a row in
 * the list screen and navigates to its own dedicated detail screen.
 *
 * @param nameResId String resource for the showcase name (list title + detail title).
 * @param descResId String resource for the one-line showcase description.
 */
@Keep
internal enum class ModelShowcase(
    @get:StringRes val nameResId: Int,
    @get:StringRes val descResId: Int,
) {
    MODELS_LIST(
        nameResId = R.string.model_sc_list_name,
        descResId = R.string.model_sc_list_desc,
    ),
    CREATE_CONSUME_COMMIT(
        nameResId = R.string.model_sc_create_name,
        descResId = R.string.model_sc_create_desc,
    ),
    SYMBOL_KEYS(
        nameResId = R.string.model_sc_symbols_name,
        descResId = R.string.model_sc_symbols_desc,
    ),
    SELECTION_EVENTS(
        nameResId = R.string.model_sc_selection_name,
        descResId = R.string.model_sc_selection_desc,
    ),
}
