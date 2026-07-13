package vision.combat.c4.ds.sample.gallery.toolmanagement.managed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.toolmanagement.managed.ui.DemoWindowContent
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Hidden demo tool with a single required [ToolComponent.Window], used by
 * [vision.combat.c4.ds.sample.gallery.toolmanagement.ui.ToolManagementViewModel] to demonstrate the
 * observable difference between [vision.combat.c4.ds.sdk.tool.ToolManager.Companion.FLAG_COMPONENT_ON_TOP]
 * and [vision.combat.c4.ds.sdk.tool.ToolManager.Companion.FLAG_NONE] when activating a tool whose
 * window competes with another tool's required window (Tool Management's own).
 *
 * The activation flag used is passed in via [ToolParams] under [PARAM_OPENED_WITH_REPLACE_FLAG] so
 * the window content can render which mode opened it.
 */
internal class DemoWindowTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    private var openedWithReplaceFlag by mutableStateOf(params?.get<Boolean>(PARAM_OPENED_WITH_REPLACE_FLAG) ?: false)

    override val window: ToolComponent.Window by requiredComponent {
        DemoWindowContent(openedWithReplaceFlag)
    }

    override fun onUpdate(toolParams: ToolParams?) {
        super.onUpdate(toolParams)
        openedWithReplaceFlag = toolParams?.get<Boolean>(PARAM_OPENED_WITH_REPLACE_FLAG) ?: openedWithReplaceFlag
    }

    companion object {
        const val PARAM_OPENED_WITH_REPLACE_FLAG = "opened_with_replace_flag"
    }
}
