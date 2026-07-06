import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

android {
    namespace = "vision.combat.c4.ds.sample.bookmarks.data"
    compileSdk = 37

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":bookmarks:domain"))
    compileOnly(libs.combat.ds.sdk)

    testImplementation(libs.combat.ds.sdk)
    testImplementation(libs.junit5.jupiter.api)
    testImplementation(libs.junit5.jupiter.params)
    testImplementation(libs.kotlinx.coroutines.test)
    // Real org.json impl for the unit-test JVM classpath: android.jar's org.json classes are
    // stubs that throw/return null, so BookmarkRepositoryImpl's JSONArray/JSONObject usage
    // needs this to actually run under plain JUnit (no Robolectric/instrumentation needed).
    testImplementation(libs.json.org)
    testRuntimeOnly(libs.junit5.jupiter.engine)
    testRuntimeOnly(libs.junit5.platform.launcher)
}
