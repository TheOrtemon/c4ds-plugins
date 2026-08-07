package vision.combat.c4.ds.sample.sight

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent
import vision.combat.c4.ds.sdk.tool.component

internal class SightTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val di: DI = DI {
        extend(parentDI)

        bindSingleton {
            SightToolRepository(instance(arg = SightToolDescriptor.ID))
        }
    }
    override val overlay: ToolComponent.Overlay by requiredComponent { Overlay() }
    override val window: ToolComponent.Window by component { Window() }
}
