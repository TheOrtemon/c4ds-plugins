package vision.combat.c4.ds.sample.gallery.service

import org.kodein.di.DI
import org.kodein.di.instance
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.service.ui.ServiceWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.AbstractToolService
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Demonstrates two things at once, matching the section name "Lifecycle & Services":
 *
 * - **Services** — a session-scoped [AbstractToolService] ([BadgeCounterService]) created via
 *   [ToolDescriptor.createService] that does background work and posts an unread badge on the tool
 *   list item, sharing [ServiceSharedState] with this tool through the merged service DI graph.
 * - **Lifecycle** — this tool overrides its [AbstractTool] lifecycle callbacks and records each one
 *   into the (session-scoped) [ServiceSharedState], so the window can show a live log that survives
 *   the window being closed and reopened.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolDescriptor.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractToolService.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractTool.kt
 */
class ServiceToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.service_tool_name
    override val iconResId: Int = R.drawable.ic_service
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return ServiceTool(toolContext, this, di, params)
    }

    override fun createService(toolContext: ToolContext, di: DI): AbstractToolService {
        return BadgeCounterService(toolContext, this, di)
    }
}

/**
 * [AbstractTool] for the service sample. Wires [ServiceWindow] with the [ServiceSharedState]
 * produced by [BadgeCounterService], and logs its own lifecycle callbacks into that shared state.
 */
internal class ServiceTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    private val sharedState: ServiceSharedState by instance()
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)

    override val window: ToolComponent.Window by requiredComponent {
        ServiceWindow(sharedState)
    }

    // ── Lifecycle callbacks: real tools use these to start/stop work as components appear and
    //    disappear (see DroneTool/TelemetryTool in the host). Here we simply record each one. ──

    override fun onComponentShown(component: ToolComponent) {
        super.onComponentShown(component)
        log("onComponentShown(${component.label()})")
    }

    override fun onComponentHidden(component: ToolComponent) {
        super.onComponentHidden(component)
        log("onComponentHidden(${component.label()})")
    }

    override fun onUpdate(toolParams: ToolParams?) {
        super.onUpdate(toolParams)
        log("onUpdate()")
    }

    override fun onDestroyRequested() {
        super.onDestroyRequested()
        log("onDestroyRequested()")
    }

    private fun log(event: String) {
        sharedState.logLifecycle("${timeFormat.format(Date())} — $event")
    }

    private fun ToolComponent.label(): String = when (this) {
        is ToolComponent.Window -> "Window"
        is ToolComponent.Overlay -> "Overlay"
        is ToolComponent.Status -> "Status"
        is ToolComponent.ExpandableStatus -> "Expandable Status"
        is ToolComponent.Underlay -> "Underlay"
    }
}
