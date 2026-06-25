package vision.combat.c4.ds.sample.gallery.status

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.status.ui.StatusBar
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.statusComponent

internal class StatusTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val status: ToolComponent.Status by statusComponent(
        shouldShowCoordinates = true,
        shouldShowAzimuth = true,
    ) {
        StatusBar()
    }
}

