package vision.combat.c4.ds.sample.gallery.components

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Catalog-launched sample demonstrating pull-to-refresh and swipe-to-reveal in a list.
 *
 * SDK APIs demonstrated:
 *   reveal.RevealableLazyColumn, reveal.RevealMenuButton, reveal.RevealMenuScope,
 *   refresh.PullToRefreshIndicator (via RevealableLazyColumn's onRefresh integration)
 */
class ComponentsListsToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.components_lists_tool_name
    override val iconResId: Int = R.drawable.ic_components
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return ComponentsListsTool(toolContext, this, di, params)
    }
}
