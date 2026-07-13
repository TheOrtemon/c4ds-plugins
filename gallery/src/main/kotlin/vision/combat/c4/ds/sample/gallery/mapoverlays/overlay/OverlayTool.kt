package vision.combat.c4.ds.sample.gallery.mapoverlays.overlay

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.kodein.di.DI
import org.kodein.di.compose.rememberInstance
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.mapoverlays.overlay.ui.OverlayContent
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.deactivate
import vision.combat.c4.ds.sdk.tool.endBar
import vision.combat.c4.ds.sdk.tool.requiredComponent
import vision.combat.c4.ds.sdk.ui.component.bar.endbar.EndBarActionButton

/**
 * Minimal AbstractTool wiring a ToolComponent.Overlay (OverlayContent) and an EndBar close button.
 * Deactivates itself via [ToolManager.deactivate] typed on [OverlayToolDescriptor].
 */
internal class OverlayTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val overlay: ToolComponent.Overlay by requiredComponent {
        OverlayContent()
    }

    override val endBar by endBar {
        val toolManager by rememberInstance<ToolManager>()
        EndBarActionButton(
            icon = painterResource(R.drawable.ic_overlay),
            contentDescription = stringResource(R.string.overlay_close_cd),
            onClick = { toolManager.deactivate<OverlayToolDescriptor>() },
        )
    }
}
