cmake_minimum_required(VERSION 3.22.1)

project("isolation_jni")

# Trivial JNI library that exposes a single native function used to verify
# that the host's nativeLibraryDir wiring loads the plugin .so correctly.
add_library(
    isolation_jni          # Library name — matches System.loadLibrary("isolation_jni")
    SHARED
    isolation_jni.cpp
)

# Link against the Android log library so the native function can emit logcat output.
find_library(log-lib log)
target_link_libraries(isolation_jni ${log-lib})

