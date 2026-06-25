package vision.combat.c4.ds.sample.gallery.model

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.model.ui.ModelWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Demonstrates CommonModelInteractor: observe, select, create, delete BCM models.
 *
 * SDK APIs: CommonModelInteractor (getAllModels, selectedModel, userModel,
 *           selectModel, unselectModel, createModel, deleteModel, isReadOnly),
 *           BattlespaceConceptModel, ModelId.
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

internal class ModelTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        ModelWindow()
    }
}

