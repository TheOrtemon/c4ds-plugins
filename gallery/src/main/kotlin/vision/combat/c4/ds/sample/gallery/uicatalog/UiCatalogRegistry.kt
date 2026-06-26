package vision.combat.c4.ds.sample.gallery.uicatalog

import androidx.annotation.StringRes
import vision.combat.c4.ds.sample.gallery.R

/**
 * A single component entry in the UI Catalog list.
 *
 * @param id Stable string identifier used for navigation to the detail screen.
 * @param nameResId String resource for the component name (shown as list title + detail title).
 * @param descResId String resource for the component description.
 */
internal data class UiCatalogEntry(
    val id: String,
    @param:StringRes val nameResId: Int,
    @param:StringRes val descResId: Int,
)

/**
 * Static registry of the promoted public-SDK components showcased by the UI Catalog sample.
 * Demos for each `id` live in
 * [vision.combat.c4.ds.sample.gallery.uicatalog.ui.UiCatalogDetailScreen].
 */
internal object UiCatalogRegistry {
    // Form fields (original UI Catalog entries)
    const val INLINE_MESSAGE = "inline_message"
    const val HEADER_FIELD = "header_field"
    const val EXPANDABLE_FIELD = "expandable_field"
    const val FORM_FIELD_BOX = "form_field_box"
    const val NESTED_FORM = "nested_form"
    const val HOSTILITY_SELECTOR = "hostility_selector"

    // Folded from Components Showcase — category groups
    const val BUTTONS = "buttons"
    const val INPUTS = "inputs"
    const val SELECTION = "selection"
    const val FEEDBACK = "feedback"
    const val LISTS = "lists"

    val entries: List<UiCatalogEntry> = listOf(
        // ── Form fields ──────────────────────────────────────────────────────
        UiCatalogEntry(
            id = INLINE_MESSAGE,
            nameResId = R.string.ui_catalog_inline_message_name,
            descResId = R.string.ui_catalog_inline_message_desc,
        ),
        UiCatalogEntry(
            id = HEADER_FIELD,
            nameResId = R.string.ui_catalog_header_field_name,
            descResId = R.string.ui_catalog_header_field_desc,
        ),
        UiCatalogEntry(
            id = EXPANDABLE_FIELD,
            nameResId = R.string.ui_catalog_expandable_field_name,
            descResId = R.string.ui_catalog_expandable_field_desc,
        ),
        UiCatalogEntry(
            id = FORM_FIELD_BOX,
            nameResId = R.string.ui_catalog_form_field_box_name,
            descResId = R.string.ui_catalog_form_field_box_desc,
        ),
        UiCatalogEntry(
            id = NESTED_FORM,
            nameResId = R.string.ui_catalog_nested_form_name,
            descResId = R.string.ui_catalog_nested_form_desc,
        ),
        UiCatalogEntry(
            id = HOSTILITY_SELECTOR,
            nameResId = R.string.ui_catalog_hostility_selector_name,
            descResId = R.string.ui_catalog_hostility_selector_desc,
        ),

        // ── Folded from Components Showcase ──────────────────────────────────
        UiCatalogEntry(
            id = BUTTONS,
            nameResId = R.string.ui_catalog_buttons_name,
            descResId = R.string.ui_catalog_buttons_desc,
        ),
        UiCatalogEntry(
            id = INPUTS,
            nameResId = R.string.ui_catalog_inputs_name,
            descResId = R.string.ui_catalog_inputs_desc,
        ),
        UiCatalogEntry(
            id = SELECTION,
            nameResId = R.string.ui_catalog_selection_name,
            descResId = R.string.ui_catalog_selection_desc,
        ),
        UiCatalogEntry(
            id = FEEDBACK,
            nameResId = R.string.ui_catalog_feedback_name,
            descResId = R.string.ui_catalog_feedback_desc,
        ),
        UiCatalogEntry(
            id = LISTS,
            nameResId = R.string.ui_catalog_lists_name,
            descResId = R.string.ui_catalog_lists_desc,
        ),
    )

    fun entryById(id: String): UiCatalogEntry? = entries.firstOrNull { it.id == id }
}
