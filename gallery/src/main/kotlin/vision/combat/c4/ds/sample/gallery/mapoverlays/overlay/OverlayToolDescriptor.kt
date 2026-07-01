package vision.combat.c4.ds.sample.gallery.mapoverlays.overlay

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates ToolComponent.Overlay with map and model interactors.
 *
 * SDK APIs: ToolComponent.Overlay, requiredComponent, AbstractTool.endBar,
 *           EndBarActionButton, CommonMapInteractor.selectedPosition,
 *           CommonModelInteractor.userModel, CommonLocaleSettingsInteractor.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolComponent.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractTool.kt
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonMapInteractor.kt
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonModelInteractor.kt
 */
class OverlayToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.overlay_tool_name
    override val iconResId: Int = R.drawable.ic_overlay
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return OverlayTool(toolContext, this, di, params)
    }
}
