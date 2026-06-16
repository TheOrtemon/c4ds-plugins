import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

android {
    namespace = "vision.combat.c4.ds.tool.sample.overlay"
    compileSdk = 37

    defaultConfig {
        applicationId = "vision.combat.c4.ds.tool.sample.overlay"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Limit to the two most common ABIs to keep build times reasonable.
        // arm64-v8a covers production devices; x86_64 covers emulators.
        ndk {
            abiFilters += listOf("arm64-v8a", "x86_64")
        }

        externalNativeBuild {
            cmake {
                cppFlags += "-std=c++17"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Wire CMake so Gradle knows where to find the JNI sources.
    // NDK and CMake must be installed (sdkmanager "ndk;version" "cmake;version").
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    // AGP 8+ requires jniLibs.useLegacyPackaging instead of android:extractNativeLibs in
    // the manifest. This ensures the .so is extracted to nativeLibraryDir at install time
    // so the host's PathClassLoader (libPath = nativeLibraryDir) resolves liboverlay_jni.so
    // via System.loadLibrary("overlay_jni").
    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

// exclude kotlin-stdlib from runtimeOnly configuration as it is provided by the host app
configurations {
    getByName("runtimeOnly") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
    }
}

dependencies {
    compileOnly(libs.combat.ds.sdk)
    runtimeOnly(libs.combat.ds.sdk.runtine)

    coreLibraryDesugaring(libs.android.tools.desugar)
}
