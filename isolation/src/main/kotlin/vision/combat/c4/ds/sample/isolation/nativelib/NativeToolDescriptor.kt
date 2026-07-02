package vision.combat.c4.ds.sample.isolation.nativelib

import org.kodein.di.DI
import vision.combat.c4.ds.sample.isolation.R
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams

/**
 * Demonstrates cross-APK isolation: separate classloader, native .so from plugin nativeLibraryDir,
 * plugin AssetManager via toolContext.assets.
 *
 * This tool lives in :isolation APK (applicationId vision.combat.c4.ds.sample.isolation).
 * It is launched cross-APK from :gallery's CatalogTool via ToolManager.resolveToolId(fqcn).
 *
 * SDK APIs: ToolContext.assets (plugin AssetManager), System.loadLibrary from plugin nativeLibraryDir,
 *           ToolComponent.Window.
 *
 * SDK files:
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/ToolContext.kt
 *   c4ds-sdk/src/main/kotlin/vision/combat/c4/ds/sdk/tool/AbstractTool.kt
 */
class NativeToolDescriptor(toolContext: ToolContext) : ToolDescriptor(toolContext) {
    override val nameResId: Int = R.string.native_tool_name
    override val iconResId: Int = R.drawable.ic_isolation
    override val categories: List<String> = emptyList()

    override fun createTool(toolContext: ToolContext, di: DI, params: ToolParams?): AbstractTool {
        return NativeTool(toolContext, this, di, params)
    }
}
