package vision.combat.c4.ds.sample.gallery.toolmanagement.managed

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Hidden descriptor for [DemoWindowTool] — used by the Tool Management sample's
 * "Window Activation Flags" section to demonstrate
 * [vision.combat.c4.ds.sdk.tool.ToolManager.Companion.FLAG_COMPONENT_ON_TOP] vs
 * [vision.combat.c4.ds.sdk.tool.ToolManager.Companion.FLAG_NONE].
 *
 * `categories = emptyList()` keeps this tool out of the catalog/launcher: it exists solely to be
 * driven programmatically from [vision.combat.c4.ds.sample.gallery.toolmanagement.ui.ToolManagementViewModel].
 */
class DemoWindowToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.tool_management_demo_window_name
    override val iconResId: Int = R.drawable.ic_tool_management
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return DemoWindowTool(toolContext, this, di, params)
    }
}
