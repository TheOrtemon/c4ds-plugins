package vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Hidden descriptor for [DemoOverlayBTool] — the alternate default overlay that displaces
 * [DemoOverlayADescriptor]'s overlay while active, in the "Overlay isDefault" sample.
 *
 * `categories = emptyList()` keeps this tool out of the catalog/launcher: it exists solely to be
 * driven programmatically from
 * [vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.ui.OverlayDefaultViewModel].
 */
class DemoOverlayBDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.overlay_default_b_name
    override val iconResId: Int = R.drawable.ic_overlay
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return DemoOverlayBTool(toolContext, this, di, params)
    }
}
