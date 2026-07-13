package vision.combat.c4.ds.sample.bookmarks

import org.kodein.di.DI
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Bookmarks sample: an isolated-Room-database "bookmarks" list (add a labelled string,
 * list them, clear) split across three Gradle modules —
 * `:bookmarks:domain` (`com.android.library`), `:bookmarks:data` (`com.android.library`), and
 * `:bookmarks:app` (this module, the tool APK) — to demonstrate UI → Domain ← Data
 * dependency inversion across real module boundaries, not just package convention.
 *
 * SDK APIs demonstrated:
 *   - An isolated Room database stored under the user-scoped directory obtained from
 *     [vision.combat.c4.ds.sdk.domain.interactor.CommonSessionStorageInteractor] (see
 *     `di/BookmarksModule.kt`)
 */
class BookmarksToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.bookmarks_tool_name
    override val iconResId: Int = R.drawable.ic_bookmarks
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return BookmarksTool(toolContext, this, di, params)
    }
}
