package vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates the automatic replace/restore behavior of default
 * [vision.combat.c4.ds.sdk.tool.ToolComponent.Overlay] components: activating a second tool whose
 * overlay is also `isDefault = true` displaces the first tool's default overlay, and deactivating
 * the second tool automatically restores the first tool's default overlay — no manual
 * `showComponent`/`hideComponent` needed.
 *
 * Two hidden demo tools, two teaching-point overlays:
 * - [vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed.DemoOverlayADescriptor] —
 *   the initially-shown default overlay, auto-shown when Demo A activates.
 * - [vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed.DemoOverlayBDescriptor] —
 *   the alternate default overlay: activating Demo B auto-shows its overlay, which displaces Demo
 *   A's; deactivating Demo B auto-restores Demo A's overlay.
 *
 * SDK APIs: ToolComponent.Overlay, component(isDefault=true), requiredComponent,
 *           ToolManager.activate, ToolManager.deactivate, ToolManager.isActive,
 *           ToolManager.activeTools
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolManager.kt
 */
class OverlayDefaultToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.overlay_default_tool_name
    override val iconResId: Int = R.drawable.ic_overlay
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return OverlayDefaultTool(toolContext, this, di, params)
    }
}
