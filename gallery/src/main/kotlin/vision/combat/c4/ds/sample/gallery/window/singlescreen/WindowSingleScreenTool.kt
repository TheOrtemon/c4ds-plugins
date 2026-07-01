package vision.combat.c4.ds.sample.gallery.window.singlescreen

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.window.singlescreen.ui.WindowSingleScreenWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Minimal [AbstractTool] subclass that wires [WindowSingleScreenWindow]
 * as its single [ToolComponent.Window].
 */
internal class WindowSingleScreenTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        WindowSingleScreenWindow()
    }
}
