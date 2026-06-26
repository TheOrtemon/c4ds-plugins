package vision.combat.c4.ds.sample.gallery.mapwindow

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates ToolComponent.MapWindow with embedded map controls.
 *
 * The host [ToolWindowScreen] already provides default zoom (and orientation) buttons
 * via ToolWindowMapEndBar; [MapWindow.mapEndBarButtons] is for supplemental actions only.
 *
 * SDK APIs: ToolComponent.MapWindow, mapWindow { } factory, MapView, MapController,
 *           MapWindow.mapEndBarButtons, MapWindow.navBarContent, MapWindow.focusCameraOn,
 *           MapController.InteractionMode.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/map/MapView.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/map/MapController.kt
 */
class MapWindowToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.mapwindow_tool_name
    override val iconResId: Int = R.drawable.ic_mapwindow
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return MapWindowTool(toolContext, this, di, params)
    }
}

