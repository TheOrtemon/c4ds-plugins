package vision.combat.c4.ds.sample.gallery.hostservices

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates three host-provided, plugin-accessible SDK services: sharing links, coordinates,
 * files, and images via [vision.combat.c4.ds.sdk.ui.platform.ShareManager] /
 * [vision.combat.c4.ds.sdk.ui.platform.LocalShareManager], clipboard access via Compose's own
 * `LocalClipboard`, and in-app notifications via
 * [vision.combat.c4.ds.sdk.ui.platform.InAppNotificationManager] resolved through DI.
 *
 * Unlike the `:fileshare` sample — which had to bundle `androidx.core` because its
 * manifest-declared `FileProvider` runs in the plugin's own process — none of the three services
 * demonstrated here need any bundled dependency. This tool's UI code runs in the HOST process (the
 * normal case for `compileOnly(c4ds-sdk)` plugin code), so all three services arrive for free:
 *  - `ShareManager` is provided as a composition local (`LocalShareManager`) by the host's own
 *    `MainScreen`; the interface itself ships in `c4ds-sdk-core:ui`, which `c4ds-sdk` re-exports
 *    via `api(...)` — so it is visible to plugins even though `c4ds-sdk` itself is `compileOnly`.
 *  - `LocalClipboard` is a Compose platform composition local, already on the host's classpath.
 *  - `InAppNotificationManager` is resolved via Kodein DI (`rememberInstance`); the binding is
 *    inherited from the host's DI graph, so this plugin never has to bind it itself.
 *
 * SDK files:
 *   c4ds-sdk-core/ui/src/main/kotlin/vision/combat/c4/ds/sdk/ui/platform/ShareManager.kt
 *   c4ds-sdk-core/ui/src/main/kotlin/vision/combat/c4/ds/sdk/ui/platform/InAppNotificationManager.kt
 */
class HostServicesToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.host_services_tool_name
    override val iconResId: Int = R.drawable.ic_share
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return HostServicesTool(toolContext, this, di, params)
    }
}
