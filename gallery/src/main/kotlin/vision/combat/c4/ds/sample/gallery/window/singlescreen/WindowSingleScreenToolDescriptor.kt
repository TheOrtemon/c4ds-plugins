package vision.combat.c4.ds.sample.gallery.window.singlescreen

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates a minimal single-screen window tool with a ViewModel-backed counter.
 *
 * SDK APIs: ToolComponent.Window, WindowScaffold, BackNavTopAppBar, diViewModel(), showToast.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractTool.kt
 *   c4ds-sdk-core/ui/src/main/kotlin/vision/combat/c4/ds/sdk/ui/component/WindowScaffold.kt
 */
class WindowSingleScreenToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.window_single_screen_tool_name
    override val iconResId: Int = R.drawable.ic_window
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return WindowSingleScreenTool(toolContext, this, di, params)
    }
}
