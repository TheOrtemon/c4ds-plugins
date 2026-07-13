package vision.combat.c4.ds.sample.gallery.resources.config

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.resources.config.ui.ConfigWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Minimal [AbstractTool] subclass for the config-resources showcase; wires [ConfigWindow]
 * which exercises config-qualified string/drawable/font/raw resources.
 */
internal class ConfigTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        ConfigWindow()
    }
}
