package vision.combat.c4.ds.sample.gallery.catalog

import androidx.annotation.StringRes
import vision.combat.c4.ds.sample.gallery.R

/**
 * Sections grouping samples in the catalog list.
 */
enum class SampleSection(@StringRes val titleResId: Int) {
    WINDOWS(R.string.section_windows),
    COMPONENTS(R.string.section_components),
    MAP(R.string.section_map),
    STATUS_AND_BARS(R.string.section_status_bars),
    MODEL_AND_LIFECYCLE(R.string.section_model_lifecycle),
    RESOURCES_AND_ISOLATION(R.string.section_resources_isolation),
}

