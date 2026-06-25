package vision.combat.c4.ds.example.tool.window.endbar

import org.kodein.di.DI
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.tool.sample.window.R

class EndBarToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {

    override val iconResId: Int = R.drawable.ic_endbar
    override val nameResId: Int = R.string.endbar_tool_name

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return EndBarTool(toolContext, this, di, params)
    }
}
