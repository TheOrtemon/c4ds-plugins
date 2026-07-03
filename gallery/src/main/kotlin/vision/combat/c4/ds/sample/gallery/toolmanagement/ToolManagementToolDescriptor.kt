package vision.combat.c4.ds.sample.gallery.toolmanagement

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates [vision.combat.c4.ds.sdk.tool.ToolManager] APIs:
 * activate, deactivate, isActive, activeTools, showComponent, hideComponent, and the
 * FLAG_COMPONENT_ON_TOP / FLAG_NONE activation flags.
 *
 * Three sections, three teaching points:
 * - **Tool activation** targets a dedicated hidden demo tool
 *   ([vision.combat.c4.ds.sample.gallery.toolmanagement.managed.DemoToolDescriptor]) whose surfaces
 *   are a map overlay badge and a status strip, so activating it visibly proves the tool is active
 *   without opening any panel window — the Tool Management window and its buttons stay usable
 *   throughout.
 * - **Component activation** shows/hides that same demo tool's overlay and status components
 *   individually, distinct from whole-tool activate/deactivate.
 * - **Window activation flags** targets a second hidden demo tool with its own required window
 *   ([vision.combat.c4.ds.sample.gallery.toolmanagement.managed.DemoWindowToolDescriptor]) to
 *   contrast FLAG_COMPONENT_ON_TOP (stacks on top, Tool Management stays active) against FLAG_NONE
 *   (replaces the window stack, deactivating Tool Management because its window is required).
 *
 * SDK APIs: ToolManager.activate, ToolManager.deactivate, ToolManager.isActive,
 *           ToolManager.activeTools, ToolManager.showComponent, ToolManager.hideComponent,
 *           ToolManager.FLAG_COMPONENT_ON_TOP, ToolManager.FLAG_NONE
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
