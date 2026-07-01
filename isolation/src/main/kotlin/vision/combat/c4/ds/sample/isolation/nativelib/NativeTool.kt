package vision.combat.c4.ds.sample.isolation.nativelib

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.kodein.di.DI
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

internal class NativeTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    private val _jniResult = MutableStateFlow<String?>(null)
    private val _assetResult = MutableStateFlow<String?>(null)

    val jniResult = _jniResult.asStateFlow()
    val assetResult = _assetResult.asStateFlow()

    override val window: ToolComponent.Window by requiredComponent {
        NativeToolWindow(jniResult, assetResult)
    }

    override fun onComponentShown(component: ToolComponent) {
        super.onComponentShown(component)
        if (component is ToolComponent.Window) {
            smokeTestAssets()
            smokeTestNativeLib()
        }
    }

    /**
     * Reads `assets/isolation/sample.txt` from **this plugin's** AssetManager to verify that
     * `ToolContext.assets` returns the plugin-scoped `AssetManager` (not the host's).
     *
     * Expected logcat output:
     * ```
     * NativeTool: [ASSET SMOKE] Read 'isolation/sample.txt' from :isolation plugin — isolation OK
     * ```
     */
    private fun smokeTestAssets() {
        runCatching {
            val content = toolContext.assets.open("isolation/sample.txt")
                .use { it.readBytes() }
                .decodeToString()
            Log.i(TAG, "[ASSET SMOKE] Read 'isolation/sample.txt' from :isolation plugin. Content prefix: ${content.take(80)}")
            _assetResult.value = content.trim()
        }.onFailure { e ->
            Log.e(TAG, "[ASSET SMOKE] FAILED — check ToolContext.getAssets() isolation", e)
            _assetResult.value = "FAILED: ${e.message}"
        }
    }

    /**
     * Calls the trivial JNI function [IsolationNative.nativeVersion] to verify that
     * `libisolation_jni.so` was extracted to the plugin's `nativeLibraryDir` and that
     * the host's cached [dalvik.system.PathClassLoader] passes that directory as `libPath`.
     *
     * Expected logcat output:
     * ```
     * NativeTool: [JNI SMOKE] nativeVersion() = 'isolation-jni/1.0' — .so loaded from plugin OK
     * ```
     */
    private fun smokeTestNativeLib() {
        IsolationNative.tryLoad()
            .onSuccess {
                runCatching {
                    val version = IsolationNative.nativeVersion()
                    Log.i(TAG, "[JNI SMOKE] nativeVersion() = '$version' — .so loaded from plugin OK")
                    _jniResult.value = version
                }.onFailure { e ->
                    Log.e(TAG, "[JNI SMOKE] FAILED to call nativeVersion() after successful load", e)
                    _jniResult.value = "FAILED: ${e.message}"
                }
            }
            .onFailure { e ->
                Log.e(TAG, "[JNI SMOKE] FAILED to load libisolation_jni.so — check nativeLibraryDir wiring", e)
                _jniResult.value = "FAILED to load: ${e.message}"
            }
    }

    private companion object {
        private const val TAG = "NativeTool"
    }
}

