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
 * SDK APIs:
 *   - [vision.combat.c4.ds.sdk.ui.component.ProvideWindowContext] — **public** API for plugin
 *     authors to re-provide their tool context inside raw M2 [AlertDialog] / [DropdownMenu] /
 *     [Popup] sub-compositions where [LocalContext] would otherwise reset to the host Activity.
 *   - Plugin-compiled: M2 Scaffold, SnackbarHost, AlertDialog, DropdownMenu, Slider.
 *
 * Internal SDK mechanism (transparent to plugin authors):
 *   c4ds-sdk-core/internal/…/CompositionFallbackContext.kt
 */
class MaterialToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.material_tool_name
    override val iconResId: Int = R.drawable.ic_material
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return MaterialTool(toolContext, this, di, params)
    }
}

/**
 * Minimal [AbstractTool] subclass for the Material showcase; wires [MaterialWindow]
 * which exercises plugin-compiled M2 widgets inside the host composition context.
 */
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

