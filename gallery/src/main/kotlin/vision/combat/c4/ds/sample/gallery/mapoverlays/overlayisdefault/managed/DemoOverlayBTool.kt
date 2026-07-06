package vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed.ui.DemoOverlayBContent
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.component

/**
 * Minimal [AbstractTool] subclass wiring a single [ToolComponent.Overlay] ([DemoOverlayBContent]) —
 * no window, no status.
 *
 * `isDefault = true` is required here: only a default component is shown automatically on tool
 * activation. If this overlay were `isDefault = false` it would render nothing when this tool
 * activates without an explicit `showComponent` call, and so could never displace
 * [vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed.DemoOverlayATool]'s
 * overlay. Because both overlays are default components, activating this tool auto-shows this
 * overlay and displaces Demo A's; deactivating this tool automatically restores Demo A's overlay
 * per the SDK's documented default-component replace/restore contract.
 */
internal class DemoOverlayBTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val overlay: ToolComponent.Overlay by component(isDefault = true) {
        DemoOverlayBContent()
    }
}
