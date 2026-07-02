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
 * The window drives [vision.combat.c4.ds.sample.gallery.toolmanagement.managed.DemoToolDescriptor],
 * a dedicated hidden tool whose only surface is a
 * [vision.combat.c4.ds.sdk.tool.ToolComponent.Overlay]. Because an overlay renders over the shared
 * map rather than in the panel, [vision.combat.c4.ds.sdk.tool.ToolManager.activate] showing that
 * tool's required components never evicts this window — unlike a window-required tool, there is
 * no front-of-stack fight to work around here.
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
