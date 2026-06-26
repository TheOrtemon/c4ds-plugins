package vision.combat.c4.ds.sample.gallery.status

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates ToolComponent.Status with host coordinate and azimuth chrome flags.
 *
 * SDK APIs: ToolComponent.Status, statusComponent, Status.shouldShowCoordinates,
 *           Status.shouldShowAzimuth.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonMapInteractor.kt
 */
class StatusToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.status_tool_name
    override val iconResId: Int = R.drawable.ic_status
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return StatusTool(toolContext, this, di, params)
    }
}

