package vision.combat.c4.ds.sample.gallery.hostlibs

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Uses three host-provided libraries that no other sample touches — Coil, Accompanist Permissions
 * and mil-sym (`armyc2.**`) — to prove they resolve at runtime in a **minified** host build. All
 * three arrive through `compileOnly(c4ds-sdk)`; this module declares no dependency of its own.
 *
 * Why this sample exists: the host is obfuscated with `-repackageclasses`, and a plugin only ever
 * carries *references* to host-provided classes (they are `compileOnly`, never in the plugin APK).
 * A library the host renames is therefore unresolvable from a plugin, and only in release builds —
 * debug is not minified, so the failure is invisible until the APK ships. The SDK's consumer
 * ProGuard rules keep the names, and this tool is what exercises them: every other gallery sample
 * uses Compose, Kodein, WorldWind, Ktor or Room, all of which fall under broad keeps that were
 * never at risk.
 *
 * SDK APIs: coil3 AsyncImage, Accompanist rememberPermissionState/PermissionStatus,
 *           armyc2 SymbolUtilities.
 *
 * See docs/reference/host-provided-libraries.md for the full catalog and versions. For network
 * calls, use the host-provided **Ktor** client — see the Network Requests sample.
 */
class HostLibsToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.host_libs_tool_name
    override val iconResId: Int = R.drawable.ic_host_libs
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return HostLibsTool(toolContext, this, di, params)
    }
}
