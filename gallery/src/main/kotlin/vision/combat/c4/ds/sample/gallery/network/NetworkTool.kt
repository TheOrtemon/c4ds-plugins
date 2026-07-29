package vision.combat.c4.ds.sample.gallery.network

import org.kodein.di.DI
import org.kodein.di.subDI
import vision.combat.c4.ds.sample.gallery.network.di.networkModule
import vision.combat.c4.ds.sample.gallery.network.ui.NetworkWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Minimal [AbstractTool] subclass for the network showcase; imports [networkModule] (which
 * binds the tool's own Ktor `HttpClient`) and wires [NetworkWindow] as the single window
 * component.
 */
internal class NetworkTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val di: DI = subDI(super.di) { import(networkModule) }

    override val window: ToolComponent.Window by requiredComponent {
        NetworkWindow()
    }
}
