package vision.combat.c4.ds.sample.gallery.mapoverlays.expandablestatus

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates ToolComponent.ExpandableStatus; expand/collapse via the host chevron above
 * the status bar (end-bar toggle duplicates that control for programmatic demo).
 *
 * SDK APIs: ToolComponent.ExpandableStatus, expandableStatusComponent,
 *           ExpandableStatus.isExpanded, ExpandableStatus.shouldShowAbove,
 *           AbstractTool.endBar.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolEndBar.kt
 */
class ExpandableStatusToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.expandable_status_tool_name
    override val iconResId: Int = R.drawable.ic_expandable_status
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return ExpandableStatusTool(toolContext, this, di, params)
    }
}

