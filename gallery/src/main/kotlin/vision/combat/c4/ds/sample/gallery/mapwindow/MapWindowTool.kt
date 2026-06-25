package vision.combat.c4.ds.sample.gallery.mapwindow

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.map.MapController
import vision.combat.c4.ds.sdk.map.MapView
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.mapWindow
import vision.combat.c4.ds.sdk.ui.component.button.TextButton
import vision.combat.c4.ds.sdk.ui.component.bar.endbar.EndBarActionButton

internal class MapWindowTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    // Captured during initialize so composable lambdas can reach it via onClick handlers.
    private var mapViewRef: MapView? = null

    override val window: ToolComponent.MapWindow by mapWindow(
        isRequired = { true },
        showMapOnActivation = true,
        mapEndBarButtons = { ZoomButtons() },
        navBarContent = { ModeSelector() },
        initialize = {
            mapViewRef = this
            navigationController.interactionMode = MapController.InteractionMode.LookAt
        },
    )

    @Composable
    private fun ZoomButtons() {
        EndBarActionButton(
            icon = rememberVectorPainter(Icons.Default.Add),
            contentDescription = stringResource(R.string.mapwindow_zoom_in),
            onClick = { mapViewRef?.navigationController?.zoomIn() },
        )
        EndBarActionButton(
            icon = rememberVectorPainter(Icons.Default.Remove),
            contentDescription = stringResource(R.string.mapwindow_zoom_out),
            onClick = { mapViewRef?.navigationController?.zoomOut() },
        )
    }

    @Composable
    private fun ModeSelector() {
        TextButton(
            label = stringResource(R.string.mapwindow_mode_lookat),
            onClick = {
                mapViewRef?.navigationController?.interactionMode = MapController.InteractionMode.LookAt
            },
        )
        TextButton(
            label = stringResource(R.string.mapwindow_mode_fpv),
            onClick = {
                mapViewRef?.navigationController?.interactionMode = MapController.InteractionMode.FPV
            },
        )
    }
}
