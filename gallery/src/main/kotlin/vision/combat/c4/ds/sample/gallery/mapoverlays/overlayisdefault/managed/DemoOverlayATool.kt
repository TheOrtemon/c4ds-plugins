package vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed.ui.DemoOverlayAContent
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.component

/**
 * Minimal [AbstractTool] subclass wiring a single [ToolComponent.Overlay] ([DemoOverlayAContent]) —
 * no window, no status. Declared `isDefault = true` so it is automatically shown the moment this
 * tool is activated, and automatically restored after
 * [vision.combat.c4.ds.sample.gallery.mapoverlays.overlayisdefault.managed.DemoOverlayBTool]'s
 * overlay (which displaces it while Demo B is active) is hidden again — the SDK's documented
 * default-component replace/restore contract, with no manual `showComponent`/`hideComponent`
 * orchestration required from the sample.
 */
internal class DemoOverlayATool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val overlay: ToolComponent.Overlay by component(isDefault = true) {
        DemoOverlayAContent()
    }
}
