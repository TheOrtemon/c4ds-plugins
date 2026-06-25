#include <jni.h>
#include <android/log.h>
#include <string>

#define TAG "IsolationJNI"

extern "C" {

/**
 * Native smoke-test function called from NativeTool.onComponentShown().
 *
 * Returns a version string that the Kotlin side logs to verify:
 * 1. The plugin .so was extracted to nativeLibraryDir correctly.
 * 2. System.loadLibrary("isolation_jni") resolves the plugin library, not a host library.
 * 3. A real JNI call round-trips through the plugin classloader boundary.
 *
 * JNI naming convention:
 *   Java_<package_underscored>_<class>_<method>
 *   Package: vision.combat.c4.ds.sample.isolation.nativelib
 *          → vision_combat_c4_ds_sample_isolation_nativelib
 */
JNIEXPORT jstring JNICALL
Java_vision_combat_c4_ds_sample_isolation_nativelib_IsolationNative_nativeVersion(JNIEnv *env, jclass /*clazz*/) {
    const char *version = "isolation-jni/1.0";
    __android_log_print(ANDROID_LOG_INFO, TAG,
                        "[JNI SMOKE] nativeVersion() called — .so loaded from plugin nativeLibraryDir OK (%s)",
                        version);
    return env->NewStringUTF(version);
}

} // extern "C"

