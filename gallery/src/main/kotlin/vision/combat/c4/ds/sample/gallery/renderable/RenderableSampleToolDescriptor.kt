package vision.combat.c4.ds.sample.gallery.renderable

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates building WorldWind renderables on an AbstractMapTool: a point (Placemark),
 * polyline (Path), polygon (Polygon), circle (Ellipse) and text (Label). The tool seeds one of
 * each around the Kyiv demo center, lets the user add more via a status panel, and drops a point
 * wherever the terrain is tapped.
 *
 * SDK APIs: AbstractMapTool.addRenderable/removeRenderable, CommonMapInteractor.requestRedraw,
 *           CommonMapInteractor.focusOnLocation, ToolComponent.Status,
 *           WorldWind Placemark/Path/Polygon/Ellipse/Label.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractMapTool.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonMapInteractor.kt
 */
class RenderableSampleToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.renderable_tool_name
    override val iconResId: Int = R.drawable.ic_map
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return RenderableSampleTool(toolContext, this, di, params)
    }
}
