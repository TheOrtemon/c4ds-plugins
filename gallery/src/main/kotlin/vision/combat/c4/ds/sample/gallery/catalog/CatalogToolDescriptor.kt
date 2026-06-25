package vision.combat.c4.ds.sample.gallery.catalog

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.catalog.ui.CatalogWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Hub tool — the only launcher-visible entry point.
 *
 * All other sample tools set [categories] = emptyList() and are launched
 * from this catalog via [vision.combat.c4.ds.sdk.tool.ToolManager]. This descriptor demonstrates:
 *   - [ToolDescriptor.categories] — CATEGORY_LAUNCHER controls host launcher visibility
 *   - [ToolDescriptor.CATEGORY_LAUNCHER] constant
 *
 * SDK files: c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolDescriptor.kt
 */
class CatalogToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.catalog_tool_name
    override val iconResId: Int = R.drawable.ic_catalog
    // categories = listOf(CATEGORY_LAUNCHER) is the default — explicit here for documentation
    override val categories: List<String> = listOf(CATEGORY_LAUNCHER)

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return CatalogTool(toolContext, this, di, params)
    }
}

internal class CatalogTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        CatalogWindow()
    }
}

