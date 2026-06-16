cmake_minimum_required(VERSION 3.22.1)

project("overlay_jni")

# Trivial JNI library that exposes a single native function used to verify
# that the host's nativeLibraryDir wiring loads the plugin .so correctly.
add_library(
    overlay_jni          # Library name — matches System.loadLibrary("overlay_jni")
    SHARED
    overlay_jni.cpp
)

# Link against the Android log library so the native function can emit logcat output.
find_library(log-lib log)
target_link_libraries(overlay_jni ${log-lib})
