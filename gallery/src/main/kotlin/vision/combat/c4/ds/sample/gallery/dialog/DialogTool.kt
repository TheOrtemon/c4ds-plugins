package vision.combat.c4.ds.sample.gallery.dialog

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.dialog.ui.DialogWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolDialog
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Minimal [AbstractTool] subclass that wires [DialogWindow] and delegates
 * [ToolDialog] show/dismiss to the [AbstractTool] dialog API.
 */
internal class DialogTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        DialogWindow(
            onShowDialog = { dialog -> showDialog(dialog) },
            onDismissDialog = { dismissDialog() },
        )
    }
}
