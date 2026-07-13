package vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.ui.OverlayDefaultWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Minimal [AbstractTool] subclass that wires [OverlayDefaultWindow] as its single
 * [ToolComponent.Window].
 *
 * The window drives two dedicated hidden tools, each declaring its own default
 * [vision.combat.c4.ds.sdk.tool.ToolComponent.Overlay]:
 * - [vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed.DemoOverlayADescriptor] —
 *   the initially-shown default overlay.
 * - [vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed.DemoOverlayBDescriptor] —
 *   the alternate default overlay that displaces Demo A's overlay while active, and whose
 *   deactivation automatically restores Demo A's overlay.
 *
 * Both demo tools render an overlay over the shared map rather than in the panel, so
 * activating/deactivating either of them never evicts this window.
 */
internal class OverlayDefaultTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        OverlayDefaultWindow()
    }
}
