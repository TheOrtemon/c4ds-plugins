package vision.combat.c4.ds.sample.gallery.resources.collision

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.resources.collision.ui.CollisionWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Minimal [AbstractTool] subclass for the collision showcase; wires [CollisionWindow]
 * which demonstrates plugin-first R.string resolution when plugin and host share a key name.
 */
internal class CollisionTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        CollisionWindow()
    }
}
