package vision.combat.c4.ds.sample.openwith

import org.kodein.di.DI
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates the correct pattern for a plugin that needs to hand a file to another app: it does
 * **not** declare its own `FileProvider`. A plugin-owned provider can never work for this, for two
 * independent reasons (see docs/guides/getting-started.md):
 *  1. Classload — a manifest `<provider>` runs in the plugin's own process, using the plugin's own
 *     classloader; the provider's library (e.g. `androidx.core`) would have to be bundled
 *     (`implementation`) or the plugin crashes on bind (`ClassNotFoundException`).
 *  2. Grant (the real blocker) — this tool's *code* runs in the HOST process, not the plugin's,
 *     and a process can only grant a `content://` Uri whose provider it itself owns. The host does
 *     not own a plugin's provider (different UID), so `FLAG_GRANT_READ_URI_PERMISSION` is silently
 *     dropped and the target app gets a `SecurityException`. Bundling the provider's library fixes
 *     (1) but never (2) — this is why this sample replaces the old `:fileshare` sample, which only
 *     ever fixed (1).
 *
 * Instead, this sample gets a **host-owned** `content://` Uri from
 * [vision.combat.c4.ds.sdk.ui.platform.ShareManager.getShareableUri] (obtained via
 * `LocalShareManager`) — a Uri the host process can validly grant — and builds its own
 * `ACTION_VIEW` chooser intent. Compare with the `:gallery` `hostservices` sample (Section 12),
 * which shares the same kind of file via the share sheet (`ShareManager.shareFile`) instead of a
 * custom intent.
 *
 * Unlike the other hub samples in `:gallery` (which use `categories = emptyList()` and launch from
 * the Sample Gallery), this descriptor does **not** override [categories] — it inherits
 * `listOf(CATEGORY_LAUNCHER)`, so it shows directly in the host's own Tools list, the same way the
 * old `:fileshare` sample it replaces did.
 *
 * SDK APIs: `ShareManager.getShareableUri`, `LocalShareManager`, `ToolComponent.Window`.
 *
 * SDK files:
 *   c4ds-sdk-core/ui/src/main/kotlin/vision/combat/c4/ds/sdk/ui/platform/ShareManager.kt
 */
class OpenWithToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.open_with_tool_name
    override val iconResId: Int = R.drawable.ic_open_with

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return OpenWithTool(toolContext, this, di, params)
    }
}
