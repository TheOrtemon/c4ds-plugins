package vision.combat.c4.ds.sample.gallery.resources.material

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.resources.material.ui.MaterialWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Verifies that plugin-compiled Compose Material widgets work inside the host
 * via CompositionFallbackContext (no Resources$NotFoundException).
 *
 * SDK APIs: (internal) CompositionFallbackContext — transparent to plugin authors.
 *           Plugin-compiled: M2 Scaffold, SnackbarHost, AlertDialog, DropdownMenu, Slider.
 *
 * SDK files (internal, not callable from plugins):
 *   c4ds-sdk-core/host/src/main/kotlin/vision/combat/c4/ds/sdk/host/CompositionFallbackContext.kt
 */
class MaterialToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.material_tool_name
    override val iconResId: Int = R.drawable.ic_material
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return MaterialTool(toolContext, this, di, params)
    }
}

internal class MaterialTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        MaterialWindow()
    }
}

