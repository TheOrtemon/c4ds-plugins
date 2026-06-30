package vision.combat.c4.ds.sample.gallery.window.multiscreen

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates multi-screen window navigation with tool-scoped state.
 *
 * SDK APIs: AppNavHost, Route, BackNavTopAppBar, subDI(super.di) + Kodein module,
 *           tool-scoped SharedPreferences.
 *
 * SDK files:
 *   c4ds-sdk-core/ui/.../navigation/AppNavHost.kt
 *   c4ds-sdk-core/ui/.../navigation/Route.kt
 */
class WindowMultiScreenToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.window_multi_screen_tool_name
    override val iconResId: Int = R.drawable.ic_window
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return WindowMultiScreenTool(toolContext, this, di, params)
    }
}
