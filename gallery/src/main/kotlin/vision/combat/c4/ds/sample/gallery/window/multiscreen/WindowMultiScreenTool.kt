package vision.combat.c4.ds.sample.gallery.window.multiscreen

import org.kodein.di.DI
import org.kodein.di.subDI
import vision.combat.c4.ds.sample.gallery.window.multiscreen.di.windowMultiScreenModule
import vision.combat.c4.ds.sample.gallery.window.multiscreen.ui.WindowMultiScreenWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

internal class WindowMultiScreenTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val di: DI = subDI(super.di) { import(windowMultiScreenModule) }

    override val window: ToolComponent.Window by requiredComponent {
        WindowMultiScreenWindow()
    }
}
