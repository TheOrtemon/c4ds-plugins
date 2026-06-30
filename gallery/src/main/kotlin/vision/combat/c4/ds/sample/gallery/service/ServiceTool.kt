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

/**
 * Demonstrates session-scoped [AbstractToolService] lifecycle.
 *
 * SDK APIs: [ToolDescriptor.createService], `subDI(super.di)`,
 * [ToolNotificationManager.counter].
 *
 * The service is created at session start; the tool window reads the same
 * [ServiceSharedState] from the merged service DI graph when activated.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolDescriptor.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractToolService.kt
 */
class ServiceToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.service_tool_name
    override val iconResId: Int = R.drawable.ic_service
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return ServiceTool(toolContext, this, di, params)
    }

    override fun createService(toolContext: ToolContext, di: DI): AbstractToolService {
        return ServiceSampleService(toolContext, this, di)
    }
}

internal class ServiceTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    private val sharedState: ServiceSharedState by instance()

    override val window: ToolComponent.Window by requiredComponent {
        ServiceWindow(sharedState)
    }
}
