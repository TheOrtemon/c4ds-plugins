package vision.combat.c4.ds.sample.gallery.window.navigation

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates multi-screen window navigation.
 *
 * SDK APIs: AppNavHost, Route, BackNavigationButton, subDI + Kodein module,
 *           ToolManager.activate (cross-tool, same APK).
 *
 * SDK files:
 *   c4ds-sdk-core/ui/.../navigation/AppNavHost.kt
 *   c4ds-sdk-core/ui/.../navigation/Route.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolManager.kt
 */
class WindowNavToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.window_nav_tool_name
    override val iconResId: Int = R.drawable.ic_window
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return WindowNavTool(toolContext, this, di, params)
    }
}

