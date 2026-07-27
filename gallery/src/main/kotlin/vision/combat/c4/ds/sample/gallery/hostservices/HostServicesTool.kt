package vision.combat.c4.ds.sample.gallery.hostservices

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.hostservices.ui.HostServicesWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Minimal [AbstractTool] subclass that wires [HostServicesWindow], passing [toolContext] through
 * so the window can stage a small file under [ToolContext.getCacheDir] for [HostServicesWindow]'s
 * `shareFile` demo.
 */
internal class HostServicesTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        HostServicesWindow(toolContext = toolContext)
    }
}
