package vision.combat.c4.ds.sample.isolation.nativelib

/**
 * JNI bridge for the `isolation_jni` native library.
 *
 * The library is loaded lazily via [tryLoad] (also called from [NativeTool.onComponentShown]).
 * Because the host passes this plugin's `nativeLibraryDir` as the `libPath` argument to the
 * cached [dalvik.system.PathClassLoader], Android's linker finds `libisolation_jni.so` in the
 * plugin's extracted native library directory — not the host's.
 *
 * This is the minimal JNI smoke that proves end-to-end:
 *   plugin APK → extract `.so` → nativeLibraryDir → PathClassLoader libPath → loadLibrary → JNI call
 */
internal object IsolationNative {

    private var loaded = false

    /**
     * Loads `libisolation_jni.so` lazily from the plugin's `nativeLibraryDir`.
     * The library name must match the CMake `add_library` target name in `CMakeLists.txt`.
     */
    fun tryLoad(): Result<Unit> = runCatching {
        if (!loaded) {
            System.loadLibrary("isolation_jni")
            loaded = true
        }
    }

    /**
     * Returns the version string from the native layer (`"isolation-jni/1.0"`).
     * Called after a successful [tryLoad] to confirm the plugin's `.so` is loaded correctly.
     */
    external fun nativeVersion(): String
}
