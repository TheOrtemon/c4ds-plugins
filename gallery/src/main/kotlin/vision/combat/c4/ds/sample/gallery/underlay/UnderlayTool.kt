package vision.combat.c4.ds.sample.gallery.underlay

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.kodein.di.DI
import org.kodein.di.compose.rememberInstance
import vision.combat.c4.ds.sample.gallery.R
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

/** Minimal [AbstractTool] subclass wiring [UnderlayContent] as a required underlay + end-bar dismiss button. */
internal class UnderlayTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val underlay: ToolComponent.Underlay by requiredComponent {
        UnderlayContent()
    }

    override val endBar by endBar {
        val toolManager by rememberInstance<ToolManager>()
        EndBarActionButton(
            icon = painterResource(R.drawable.ic_underlay),
            contentDescription = stringResource(R.string.underlay_close_cd),
            onClick = { toolManager.deactivate<UnderlayToolDescriptor>() },
        )
    }
}
