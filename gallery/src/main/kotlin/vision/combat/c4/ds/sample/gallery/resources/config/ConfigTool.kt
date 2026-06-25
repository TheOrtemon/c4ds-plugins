package vision.combat.c4.ds.sample.gallery.resources.config

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.resources.config.ui.ConfigWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Demonstrates config-qualified resources: locale, night mode, font, raw.
 *
 * SDK APIs: Plugin stringResource with values-uk/values-night/values-night-uk,
 *           LocalConfiguration (recomposition on config change),
 *           painterResource with drawable-night qualifier,
 *           FontFamily(Font(R.font.*)), context.resources.openRawResource.
 *
 * SDK files: (resource API is Android SDK; plugin context isolation is c4ds-specific)
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolContext.kt
 */
class ConfigToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.config_tool_name
    override val iconResId: Int = R.drawable.ic_resources
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return ConfigTool(toolContext, this, di, params)
    }
}

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

