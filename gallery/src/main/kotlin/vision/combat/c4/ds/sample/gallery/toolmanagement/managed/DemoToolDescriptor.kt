package vision.combat.c4.ds.sample.gallery.toolmanagement.managed

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Hidden descriptor for [DemoTool] — the target of the Tool Management sample's
 * activate/deactivate/isActive/activeTools demonstration, and of its
 * showComponent/hideComponent (component-level) demonstration.
 *
 * `categories = emptyList()` keeps this tool out of the catalog/launcher: it exists solely to be
 * driven programmatically from [vision.combat.c4.ds.sample.gallery.toolmanagement.ui.ToolManagementViewModel],
 * so activating it never evicts the Tool Management window itself (see [DemoTool]).
 */
class DemoToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.tool_management_demo_tool_name
    override val iconResId: Int = R.drawable.ic_tool_management
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return DemoTool(toolContext, this, di, params)
    }
}
