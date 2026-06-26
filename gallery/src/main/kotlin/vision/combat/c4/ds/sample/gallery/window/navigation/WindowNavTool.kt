package vision.combat.c4.ds.sample.gallery.window.navigation

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.window.navigation.di.windowNavModule
import vision.combat.c4.ds.sample.gallery.window.navigation.ui.WindowNavWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

internal class WindowNavTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val di: DI = toolSubDI { import(windowNavModule) }

    override val window: ToolComponent.Window by requiredComponent {
        WindowNavWindow()
    }
}

