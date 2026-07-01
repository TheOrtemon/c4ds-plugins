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

/**
 * Demonstrates [ToolComponent.Underlay] — composable rendered behind the map.
 *
 * This sample uses [requiredComponent] (always-on underlay). For conditional visibility,
 * use `component(isRequired = { … })` instead — see `LiveStreamTool` in c4ds.
 *
 * The host enables AR map mode while any underlay is active (`MainActivity` observes
 * underlay components). Tools that need extra setup when the underlay appears can override
 * [AbstractTool.onComponentShown] / [AbstractTool.onComponentHidden] — see `DroneTool`.
 *
 * SDK APIs: ToolComponent.Underlay, requiredComponent, AbstractTool.endBar, EndBarActionButton,
 *           ToolManager.deactivate.
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

