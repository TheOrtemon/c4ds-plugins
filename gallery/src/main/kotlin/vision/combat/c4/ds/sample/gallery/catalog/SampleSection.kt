package vision.combat.c4.ds.sample.gallery.catalog

import androidx.annotation.StringRes
import vision.combat.c4.ds.sample.gallery.R

/**
 * Sections grouping samples in the catalog list.
 *
 * Ordering here controls display order in [CatalogListScreen].
 */
enum class SampleSection(@get:StringRes val titleResId: Int) {
    WINDOWS(R.string.section_windows),
    MAP(R.string.section_map),
    MAP_OVERLAYS(R.string.section_map_overlays),
    UI_COMPONENTS(R.string.section_ui_components),
    HOST_UI(R.string.section_host_ui),
    MODEL_AND_MAP_DATA(R.string.section_model_map_data),
    LIFECYCLE(R.string.section_lifecycle),
    RESOURCES_AND_ISOLATION(R.string.section_resources_isolation),
}
