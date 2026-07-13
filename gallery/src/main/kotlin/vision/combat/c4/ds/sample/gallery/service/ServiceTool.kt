package vision.combat.c4.ds.sample.gallery.service

import org.kodein.di.DI
import org.kodein.di.instance
import vision.combat.c4.ds.sample.gallery.service.ui.ServiceWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * [AbstractTool] for the service sample. Injects its own [BadgeCounterService] from DI, hands it to
 * [ServiceWindow], and logs its own lifecycle callbacks onto it.
 */
internal class ServiceTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    // The SDK binds the tool's service into this graph (see AppToolManager: the tool DI extends the
    // service DI, and AbstractToolService binds itself via bindErasedInstance under its concrete type).
    private val service: BadgeCounterService by instance()

    override val window: ToolComponent.Window by requiredComponent {
        ServiceWindow(service)
    }

    // ── Lifecycle callbacks: real tools use these to start/stop work as components appear and
    //    disappear (see DroneTool/TelemetryTool in the host). Here we simply record each one. ──

    override fun onComponentShown(component: ToolComponent) {
        super.onComponentShown(component)
        service.logLifecycle("onComponentShown(${component.label()})")
    }

    override fun onComponentHidden(component: ToolComponent) {
        super.onComponentHidden(component)
        service.logLifecycle("onComponentHidden(${component.label()})")
    }

    override fun onUpdate(toolParams: ToolParams?) {
        super.onUpdate(toolParams)
        service.logLifecycle("onUpdate()")
    }

    override fun onDestroyRequested() {
        super.onDestroyRequested()
        service.logLifecycle("onDestroyRequested()")
    }

    private fun ToolComponent.label(): String = when (this) {
        is ToolComponent.Window -> "Window"
        is ToolComponent.Overlay -> "Overlay"
        is ToolComponent.Status -> "Status"
        is ToolComponent.ExpandableStatus -> "Expandable Status"
        is ToolComponent.Underlay -> "Underlay"
    }
}
