package vision.combat.c4.ds.sample.bookmarks

import org.kodein.di.DI
import org.kodein.di.subDI
import vision.combat.c4.ds.sample.bookmarks.di.bookmarksModule
import vision.combat.c4.ds.sample.bookmarks.ui.BookmarksWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Minimal [AbstractTool] subclass for the bookmarks sample; imports [bookmarksModule] and
 * wires [BookmarksWindow] as the single window component.
 */
internal class BookmarksTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val di: DI = subDI(super.di) { import(bookmarksModule) }

    override val window: ToolComponent.Window by requiredComponent {
        BookmarksWindow()
    }
}
