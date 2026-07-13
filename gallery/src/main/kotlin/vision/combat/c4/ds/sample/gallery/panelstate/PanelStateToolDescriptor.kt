package vision.combat.c4.ds.sample.gallery.panelstate

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates [vision.combat.c4.ds.sdk.ui.manager.PanelManager] — open (Half/Full), close,
 * and observe the live [vision.combat.c4.ds.sdk.ui.manager.PanelState] flow.
 *
 * SDK APIs: PanelManager.openPanel, PanelManager.closePanel, PanelManager.panelState,
 *           PanelState.Opened.Half, PanelState.Opened.Full, PanelState.Closed
 *
 * SDK files:
 *   c4ds-sdk-core/ui/src/main/kotlin/vision/combat/c4/ds/sdk/ui/manager/PanelManager.kt
 */
class PanelStateToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.panel_state_tool_name
    override val iconResId: Int = R.drawable.ic_panel_state
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return PanelStateTool(toolContext, this, di, params)
    }
}
