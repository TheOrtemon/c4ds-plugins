package vision.combat.c4.ds.sample.gallery.resources.material

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.resources.material.ui.MaterialWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Minimal [AbstractTool] subclass for the Material showcase; wires [MaterialWindow]
 * which exercises plugin-compiled M2 widgets inside the host composition context.
 */
internal class MaterialTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        MaterialWindow()
    }
}
