package vision.combat.c4.ds.sample.gallery.toolmanagement

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.toolmanagement.ui.ToolManagementWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Minimal [AbstractTool] subclass that wires [ToolManagementWindow] as its single
 * [ToolComponent.Window].
 *
 * The window drives two dedicated hidden tools:
 * - [vision.combat.c4.ds.sample.gallery.toolmanagement.managed.DemoToolDescriptor], whose surfaces
 *   are an [vision.combat.c4.ds.sdk.tool.ToolComponent.Overlay] and a
 *   [vision.combat.c4.ds.sdk.tool.ToolComponent.Status] — rendering over/around the shared map
 *   rather than in the panel, so activating it (or showing/hiding either component) never evicts
 *   this window.
 * - [vision.combat.c4.ds.sample.gallery.toolmanagement.managed.DemoWindowToolDescriptor], whose
 *   surface is its own required [vision.combat.c4.ds.sdk.tool.ToolComponent.Window] — activating it
 *   with [vision.combat.c4.ds.sdk.tool.ToolManager.Companion.FLAG_COMPONENT_ON_TOP] stacks it above
 *   this window, while [vision.combat.c4.ds.sdk.tool.ToolManager.Companion.FLAG_NONE] replaces this
 *   window in the stack — which, because this window is itself required, deactivates this tool.
 */
internal class ToolManagementTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        ToolManagementWindow()
    }
}
