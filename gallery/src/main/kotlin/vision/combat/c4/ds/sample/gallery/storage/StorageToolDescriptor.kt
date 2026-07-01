package vision.combat.c4.ds.sample.gallery.storage

import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Hub for three storage showcases:
 *
 * - **File** — uses [vision.combat.c4.ds.sdk.domain.interactor.CommonSessionStorageInteractor]
 *   to show root and user directory paths, write a file to the user directory off the main
 *   thread, and read it back.
 * - **Preferences** — demonstrates plugin-isolated [android.content.SharedPreferences] persisted
 *   under the SDK-managed directory.
 * - **Room** — demonstrates an isolated Room database stored under the SDK-provided user
 *   directory, with all operations running on [kotlinx.coroutines.Dispatchers.IO].
 *
 * SDK APIs demonstrated:
 *   - CommonSessionStorageInteractor.getRootDirectoryPath
 *   - CommonSessionStorageInteractor.getUserDirectoryPath
 *   - Context.getSharedPreferences (keyed by the tool descriptor's qualified name)
 *
 * SDK files:
 *   c4ds-sdk-core/domain/src/commonMain/kotlin/vision/combat/c4/ds/sdk/domain/interactor/CommonSessionStorageInteractor.kt
 */
class StorageToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.storage_tool_name
    override val iconResId: Int = R.drawable.ic_storage
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return StorageTool(toolContext, this, di, params)
    }
}
