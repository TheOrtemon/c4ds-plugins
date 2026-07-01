package vision.combat.c4.ds.sample.gallery.mapview.renderable

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates building WorldWind renderables on an AbstractMapTool: a point (Placemark),
 * polyline (Path), polygon (Polygon), circle (Ellipse) and text (Label). On init the tool seeds
 * one of each shape at the Kyiv center (CENTER_LAT/CENTER_LON) and frames the camera there.
 * Per-button add* calls place new shapes at the live cursor (selectedPosition.value). A tap on
 * the terrain drops an additional point at the tapped position.
 *
 * SDK APIs: AbstractMapTool.addRenderable/removeRenderable, CommonMapInteractor.requestRedraw,
 *           CommonMapInteractor.focusOnLocation, ToolComponent.Window,
 *           WorldWind Placemark/Path/Polygon/Ellipse/Label.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractMapTool.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonMapInteractor.kt
 */
class RenderableToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.renderable_tool_name
    override val iconResId: Int = R.drawable.ic_renderable
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return RenderableTool(toolContext, this, di, params)
    }
}
