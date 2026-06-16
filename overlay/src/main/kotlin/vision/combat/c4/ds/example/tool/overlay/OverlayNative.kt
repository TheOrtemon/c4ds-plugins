package vision.combat.c4.ds.example.tool.overlay

/**
 * JNI bridge for the `overlay_jni` native library.
 *
 * The library is loaded once per process (in the companion object initializer) via
 * [System.loadLibrary].  Because the host passes this plugin's `nativeLibraryDir` as the
 * `libPath` argument to the cached [dalvik.system.PathClassLoader], Android's linker finds
 * `liboverlay_jni.so` in the plugin's extracted native library directory — not the host's.
 *
 * This is the minimal JNI smoke that proves end-to-end:
 *   plugin APK → extract `.so` → nativeLibraryDir → PathClassLoader libPath → loadLibrary → JNI call
 */
internal object OverlayNative {

    /**
     * Returns the version string from the native layer (`"overlay-jni/1.0"`).
     * Called from [OverlayTool.onStart] to confirm the plugin's `.so` is loaded correctly.
     */
    external fun nativeVersion(): String

    /**
     * Loads `liboverlay_jni.so` from the plugin's `nativeLibraryDir`.
     * The library name must match the CMake `add_library` target name in `CMakeLists.txt`.
     */
    fun tryLoad(): Result<Unit> = runCatching { System.loadLibrary("overlay_jni") }

    init {
        tryLoad()
    }
}
