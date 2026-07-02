package vision.combat.c4.ds.sample.gallery.toolmanagement

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates [vision.combat.c4.ds.sdk.tool.ToolManager] APIs:
 * activate, deactivate, isActive, activeTools.
 *
 * The sample targets a dedicated hidden demo tool
 * ([vision.combat.c4.ds.sample.gallery.toolmanagement.managed.DemoToolDescriptor]) whose only
 * surface is a map overlay badge, so activating it visibly proves the tool is active without
 * opening any panel window — the Tool Management window and its buttons stay usable throughout.
 *
 * SDK APIs: ToolManager.activate, ToolManager.deactivate, ToolManager.isActive,
 *           ToolManager.activeTools
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolManager.kt
 */
class ToolManagementToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.tool_management_tool_name
    override val iconResId: Int = R.drawable.ic_tool_management
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return ToolManagementTool(toolContext, this, di, params)
    }
}
