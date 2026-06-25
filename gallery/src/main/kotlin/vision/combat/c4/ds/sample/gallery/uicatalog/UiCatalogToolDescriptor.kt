package vision.combat.c4.ds.sample.gallery.uicatalog

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Catalog-launched sample showcasing the public SDK form components that were promoted from the
 * internal `:form` module into the public `:ui` module.
 *
 * Navigation: a list of components -> a per-component detail screen that renders the component in
 * several states (see [vision.combat.c4.ds.sample.gallery.uicatalog.ui.UiCatalogWindow]).
 *
 * SDK APIs demonstrated:
 *   ui.component.field.InlineMessage,
 *   ui.component.field.HeaderField,
 *   ui.component.field.ExpandableField,
 *   ui.component.field.FormFieldBox,
 *   ui.component.field.NestedForm,
 *   ui.component.hostility.HostilitySelector,
 *   ui.navigation.AppNavHost, WindowScaffold, bar.BackNavTopAppBar, bar.TopAppBar
 */
class UiCatalogToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.ui_catalog_tool_name
    override val iconResId: Int = R.drawable.ic_ui_catalog
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return UiCatalogTool(toolContext, this, di, params)
    }
}
