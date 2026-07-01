package vision.combat.c4.ds.sample.gallery.mapview.map

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates AbstractMapTool: renderable layers, SelectDragCallback, map interaction.
 *
 * SDK APIs: AbstractMapTool, SelectDragCallback, addRenderable, RenderableLayer,
 *           ToolComponent.Status.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractMapTool.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolLayer.kt
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonMapInteractor.kt
 */
class MapToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.map_tool_name
    override val iconResId: Int = R.drawable.ic_map
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return MapTool(toolContext, this, di, params)
    }
}

