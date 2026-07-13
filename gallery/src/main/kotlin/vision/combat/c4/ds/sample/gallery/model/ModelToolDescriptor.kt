package vision.combat.c4.ds.sample.gallery.model

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates CommonModelInteractor: observe, select, unselect BCM models; read-only awareness.
 *
 * SDK APIs: CommonModelInteractor (getAllModels, selectedModel, userModel,
 *           selectModel, unselectModel, isReadOnly), BattlespaceConceptModel, ModelId.
 *
 * SDK files:
 *   c4ds-sdk-core/domain/src/commonMain/.../interactor/CommonModelInteractor.kt
 */
class ModelToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.model_tool_name
    override val iconResId: Int = R.drawable.ic_model
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return ModelTool(toolContext, this, di, params)
    }
}
