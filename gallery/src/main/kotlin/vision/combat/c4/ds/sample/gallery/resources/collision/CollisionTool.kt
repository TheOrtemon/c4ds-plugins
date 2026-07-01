package vision.combat.c4.ds.sample.gallery.resources.collision

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.resources.collision.ui.CollisionWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Demonstrates plugin-first R.string resolution when plugin and host share a string name.
 *
 * SDK APIs: (internal) CompositionFallbackContext, FallbackResources — transparent to plugin authors.
 *           Plugin declares R.string.settings = "Plugin Settings", colliding by name with the host's
 *           own R.string.settings ("Settings"); FallbackResources resolves the plugin id first.
 *
 * Internal SDK mechanism (transparent to plugin authors):
 *   c4ds-sdk-core/internal/…/FallbackResources.kt
 *
 * Note: [vision.combat.c4.ds.sdk.ui.component.ProvideWindowContext] is the **public** companion
 * API for popup/dialog context re-provision — not needed here since CollisionWindow uses no
 * popups, but see MaterialTool for the popup-context use case.
 */
class CollisionToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.collision_tool_name
    override val iconResId: Int = R.drawable.ic_collision
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return CollisionTool(toolContext, this, di, params)
    }
}

/**
 * Minimal [AbstractTool] subclass for the collision showcase; wires [CollisionWindow]
 * which demonstrates plugin-first R.string resolution when plugin and host share a key name.
 */
internal class CollisionTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val window: ToolComponent.Window by requiredComponent {
        CollisionWindow()
    }
}

