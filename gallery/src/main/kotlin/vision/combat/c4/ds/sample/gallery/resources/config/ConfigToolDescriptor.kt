package vision.combat.c4.ds.sample.gallery.resources.config

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates config-qualified resources: app language (values-uk), night mode
 * (values-night / drawable-night), plugin font, and raw resource.
 *
 * SDK APIs: Plugin stringResource / painterResource with configuration qualifiers,
 *           FontFamily(Font(R.font.*)), context.resources.openRawResource.
 *           Recomposition on config change comes from the host composition context
 *           (plugin [ToolContext] rebuilds resources when host Configuration/locale changes).
 *
 * SDK files: (resource API is Android SDK; plugin context isolation is c4ds-specific)
 *   c4ds-app/src/main/kotlin/vision/combat/c4/ds/platform/tool/ToolContext.kt
 */
class ConfigToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.config_tool_name
    override val iconResId: Int = R.drawable.ic_resources
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return ConfigTool(toolContext, this, di, params)
    }
}
