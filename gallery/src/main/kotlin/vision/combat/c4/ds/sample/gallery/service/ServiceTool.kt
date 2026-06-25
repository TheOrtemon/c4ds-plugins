package vision.combat.c4.ds.sample.gallery.service

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.service.ui.ServiceWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.AbstractToolService
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Demonstrates AbstractToolService + autoStart.
 *
 * SDK APIs: ToolDescriptor.createService, ToolDescriptor.autoStart = true,
 *           AbstractToolService (onStart/onStop, coroutine scope),
 *           ToolNotificationManager.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolDescriptor.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractToolService.kt
 */
class ServiceToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.service_tool_name
    override val iconResId: Int = R.drawable.ic_service
    override val categories: List<String> = emptyList()
    override val autoStart: Boolean = true

    // Shared state instance — both tool and service access it.
    private val sharedState = ServiceSharedState()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return ServiceTool(toolContext, this, di, params, sharedState)
    }

    override fun createService(toolContext: ToolContext, di: DI): AbstractToolService {
        return ServiceSampleService(toolContext, di, sharedState)
    }
}

internal class ServiceTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
    private val sharedState: ServiceSharedState,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        ServiceWindow(sharedState)
    }
}
