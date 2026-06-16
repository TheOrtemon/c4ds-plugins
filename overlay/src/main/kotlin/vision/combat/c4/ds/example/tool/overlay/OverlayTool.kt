package vision.combat.c4.ds.example.tool.overlay

import android.util.Log
import org.kodein.di.DI
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

internal class OverlayTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val overlay: ToolComponent.Overlay by requiredComponent { Overlay() }

    override fun onComponentShown(component: ToolComponent) {
        super.onComponentShown(component)
        if (component is ToolComponent.Overlay) {
            smokeTestPluginAssets()
            smokeTestNativeLibrary()
        }
    }

    /**
     * Reads `assets/overlay/sample.txt` from **this plugin's** AssetManager to verify that
     * `ToolContext.assets` returns the plugin-scoped `AssetManager` (not the host's).
     *
     * Expected output in logcat:
     * ```
     * OverlayTool: [ASSET SMOKE] Read 'overlay/sample.txt' from plugin (152 bytes) — isolation OK
     * ```
     *
     * If the host's AssetManager is used instead, the `open` call throws `FileNotFoundException`
     * because the host APK does not contain `overlay/sample.txt`.
     */
    private fun smokeTestPluginAssets() {
        runCatching {
            val content = toolContext.assets.open("overlay/sample.txt").use { it.readBytes() }.decodeToString()
            Log.i(TAG, "[ASSET SMOKE] Read 'overlay/sample.txt' from plugin. Content: $content")
        }.onFailure { e ->
            Log.e(TAG, "[ASSET SMOKE] FAILED to read 'overlay/sample.txt' — check ToolContext.getAssets() isolation", e)
        }
    }

    /**
     * Calls the trivial JNI function [OverlayNative.nativeVersion] to verify that
     * `liboverlay_jni.so` was extracted to the plugin's `nativeLibraryDir` and that
     * the host's cached [dalvik.system.PathClassLoader] passes that directory as `libPath`.
     *
     * Expected output in logcat:
     * ```
     * OverlayTool: [JNI SMOKE] nativeVersion() = 'overlay-jni/1.0' — .so loaded from plugin OK
     * ```
     *
     * If the `.so` is not extracted (e.g. `android:extractNativeLibs="false"` or the host
     * does not pass `nativeLibraryDir` to the PathClassLoader), `System.loadLibrary` throws
     * `UnsatisfiedLinkError` and this log line will show `FAILED`.
     */
    private fun smokeTestNativeLibrary() {
        OverlayNative.tryLoad()
            .onSuccess {
                runCatching {
                    val version = OverlayNative.nativeVersion()
                    Log.i(TAG, "[JNI SMOKE] nativeVersion() = '$version' — .so loaded from plugin OK")
                }.onFailure { e ->
                    Log.e(TAG, "[JNI SMOKE] FAILED to call nativeVersion() after successful load", e)
                }
            }
            .onFailure { e ->
                Log.e(TAG, "[JNI SMOKE] FAILED to load liboverlay_jni.so — check nativeLibraryDir wiring in PathClassLoader", e)
            }
    }

    private companion object {
        private const val TAG = "OverlayTool"
    }
}
