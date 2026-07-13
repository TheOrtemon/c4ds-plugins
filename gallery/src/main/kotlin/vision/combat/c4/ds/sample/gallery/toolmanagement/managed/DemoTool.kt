package vision.combat.c4.ds.sample.gallery.toolmanagement.managed

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.toolmanagement.managed.ui.DemoOverlayContent
import vision.combat.c4.ds.sample.gallery.toolmanagement.managed.ui.DemoStatusContent
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.component
import vision.combat.c4.ds.sdk.tool.statusComponent

/**
 * Minimal [AbstractTool] subclass wiring a [ToolComponent.Overlay] ([DemoOverlayContent]) and a
 * [ToolComponent.Status] ([DemoStatusContent]) — no window. Because both surfaces render over/around
 * the shared map rather than in the panel, activating this tool never evicts whatever window is
 * currently shown (e.g. the Tool Management window), which is exactly what the Tool Management
 * sample needs to demonstrate activate/deactivate/isActive/activeTools without the demo target
 * fighting for screen space.
 *
 * Both components are declared non-required so
 * [vision.combat.c4.ds.sample.gallery.toolmanagement.ui.ToolManagementViewModel] can show/hide each
 * one individually via [vision.combat.c4.ds.sdk.tool.ToolManager.showComponent] /
 * [vision.combat.c4.ds.sdk.tool.ToolManager.hideComponent] without the tool itself deactivating —
 * only hiding a *required* component triggers automatic tool deactivation. The overlay is
 * `isDefault = true` so it still appears automatically the moment the tool is activated, matching
 * the "Activate Demo Tool" section above; the status strip is opt-in via its own button.
 */
internal class DemoTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val overlay: ToolComponent.Overlay by component(isDefault = true) {
        DemoOverlayContent()
    }

    override val status: ToolComponent.Status by statusComponent(
        isDefault = false,
    ) {
        DemoStatusContent()
    }
}
