#include <jni.h>
#include <android/log.h>
#include <string>

#define TAG "OverlayJNI"

extern "C" {

/**
 * Native smoke-test function called from OverlayTool.onStart().
 *
 * Returns a greeting string that the Kotlin side logs to verify:
 * 1. The plugin .so was extracted to nativeLibraryDir correctly.
 * 2. System.loadLibrary("overlay_jni") resolves the plugin library, not a host library.
 * 3. A real JNI call round-trips through the plugin classloader boundary.
 *
 * JNI naming convention:
 *   Java_<package_underscored>_<class>_<method>
 *   Package: vision.combat.c4.ds.example.tool.overlay → vision_combat_c4_ds_example_tool_overlay
 */
JNIEXPORT jstring JNICALL
Java_vision_combat_c4_ds_example_tool_overlay_OverlayNative_nativeVersion(JNIEnv *env, jclass /*clazz*/) {
    const char *version = "overlay-jni/1.0";
    __android_log_print(ANDROID_LOG_INFO, TAG,
                        "[JNI SMOKE] nativeVersion() called — .so loaded from plugin nativeLibraryDir OK (%s)",
                        version);
    return env->NewStringUTF(version);
}

} // extern "C"
