package vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Hidden descriptor for [DemoOverlayATool] — the initially-shown default overlay in the
 * "Overlay isDefault" sample.
 *
 * `categories = emptyList()` keeps this tool out of the catalog/launcher: it exists solely to be
 * driven programmatically from
 * [vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.ui.OverlayDefaultViewModel].
 */
class DemoOverlayADescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.overlay_default_a_name
    override val iconResId: Int = R.drawable.ic_overlay
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return DemoOverlayATool(toolContext, this, di, params)
    }
}
