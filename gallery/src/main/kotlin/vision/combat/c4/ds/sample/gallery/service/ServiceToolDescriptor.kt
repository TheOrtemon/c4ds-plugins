package vision.combat.c4.ds.sample.gallery.service

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.AbstractToolService
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates two things at once, matching the section name "Lifecycle & Services":
 *
 * - **Services** — a session-scoped [AbstractToolService] ([BadgeCounterService]) created via
 *   [ToolDescriptor.createService] that does background work and posts an unread badge on the tool
 *   list item. The tool reaches it straight from DI (see [ServiceTool.service]) — no shared-state
 *   object in between — because the SDK binds each service into its tool's graph.
 * - **Lifecycle** — this tool overrides its [AbstractTool] lifecycle callbacks and records each one
 *   on the (session-scoped) service, so the window can show a live log that survives the window
 *   being closed and reopened.
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

    override val serviceDescriptionResId = R.string.service_service_description

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return ServiceTool(toolContext, this, di, params)
    }

    override fun createService(toolContext: ToolContext, di: DI): AbstractToolService {
        return BadgeCounterService(toolContext, this, di)
    }
}
