import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// Fallback to `com.android.library` instead of a pure `org.jetbrains.kotlin.jvm` module: the
// published c4ds-sdk artifact only exposes Android/AAR variants (no JVM variant), so
// `compileOnly(libs.combat.ds.sdk)` cannot resolve on a plain-JVM compile classpath. See
// docs/architecture referenced in the plan's "Risks / open items" section.
plugins {
    alias(libs.plugins.android.library)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

android {
    namespace = "vision.combat.c4.ds.sample.bookmarks.domain"
    compileSdk = 37

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    compileOnly(libs.combat.ds.sdk)
}
