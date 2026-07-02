package vision.combat.c4.ds.sample.gallery.toolmanagement.managed

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.toolmanagement.managed.ui.DemoOverlayContent
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Minimal [AbstractTool] subclass wiring only a [ToolComponent.Overlay] ([DemoOverlayContent]) —
 * no window, no status. Because an overlay renders over the shared map rather than in the panel,
 * activating this tool never evicts whatever window is currently shown (e.g. the Tool Management
 * window), which is exactly what the Tool Management sample needs to demonstrate
 * activate/deactivate/isActive/activeTools without the demo target fighting for screen space.
 */
internal class DemoTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val overlay: ToolComponent.Overlay by requiredComponent {
        DemoOverlayContent()
    }
}
