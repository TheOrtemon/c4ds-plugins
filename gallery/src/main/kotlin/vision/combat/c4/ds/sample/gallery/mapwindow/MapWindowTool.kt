package vision.combat.c4.ds.sample.gallery.mapwindow

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.mapwindow.ui.MapWindowEndBarButtons
import vision.combat.c4.ds.sample.gallery.mapwindow.ui.MapWindowNavBar
import vision.combat.c4.ds.sdk.map.MapController
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.mapWindow

internal class MapWindowTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.MapWindow by mapWindow(
        showMapOnActivation = true,
        mapEndBarButtons = { MapWindowEndBarButtons(navigationController) },
        navBarContent = { MapWindowNavBar(navigationController) },
        initialize = {
            navigationController.interactionMode = MapController.InteractionMode.LookAt
        },
    )
}

