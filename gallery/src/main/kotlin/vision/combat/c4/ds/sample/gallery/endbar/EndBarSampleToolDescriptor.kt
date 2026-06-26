package vision.combat.c4.ds.sample.gallery.endbar

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates [vision.combat.c4.ds.sdk.tool.AbstractTool.endBar] — the tool-scoped end bar slot (action, toggle, menu buttons).
 *
 * Map-window end-bar buttons are a separate API: [vision.combat.c4.ds.sdk.tool.ToolComponent.MapWindow] `mapEndBarButtons`
 * (see the Map Window sample).
 *
 * SDK APIs: AbstractTool.endBar, EndBarActionButton, EndBarToggleButton,
 *           EndBarMenuButton, EndBarMenuScope.Checkable, EndBarMenuScope.Slider,
 *           plugin painterResource on EndBar icons (FallbackResources).
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolEndBar.kt
 *   c4ds-sdk-core/ui/src/main/kotlin/vision/combat/c4/ds/sdk/ui/component/bar/endbar/EndBar.kt
 */
class EndBarSampleToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.endbar_tool_name
    override val iconResId: Int = R.drawable.ic_end_bar
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return EndBarSampleTool(toolContext, this, di, params)
    }
}
