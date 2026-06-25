package vision.combat.c4.ds.sample.gallery.underlay

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Demonstrates ToolComponent.Underlay — composable rendered behind the map.
 *
 * SDK APIs: ToolComponent.Underlay, requiredComponent.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractTool.kt
 */
class UnderlayToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.underlay_tool_name
    override val iconResId: Int = R.drawable.ic_underlay
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return UnderlayTool(toolContext, this, di, params)
    }
}

internal class UnderlayTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val underlay: ToolComponent.Underlay by requiredComponent {
        UnderlayContent()
    }
}

