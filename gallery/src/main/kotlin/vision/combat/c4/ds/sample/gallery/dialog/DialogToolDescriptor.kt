package vision.combat.c4.ds.sample.gallery.dialog

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates all four ToolDialog types.
 *
 * SDK APIs: ToolDialog.Confirmation, .Destructive, .Info, .Custom,
 *           AbstractTool.showDialog(), AbstractTool.dismissDialog().
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolDialog.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractTool.kt
 */
class DialogToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.dialog_tool_name
    override val iconResId: Int = R.drawable.ic_dialog
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return DialogTool(toolContext, this, di, params)
    }
}

