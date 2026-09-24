@file:Suppress("UNUSED_VARIABLE", "OPT_IN_USAGE")

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.multiplatorm)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
//    explicitApi = ExplicitApiMode.Strict
    applyDefaultHierarchyTemplate()

    androidTarget {
        publishLibraryVariants("release")
    }

    jvm("desktop")

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "webview"
            isStatic = true
        }
        iosTarget.setUpiOSObserver()
    }

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.components.resources)
            implementation(libs.kermit)
            implementation(libs.kotlin.coroutines.core)
            implementation(libs.kotlin.serialization.json)
        }

        androidMain.dependencies {
            api(libs.android.activity.compose)
            api(libs.android.webkit)
            implementation(libs.kotlin.coroutines.android)
        }

        iosMain.dependencies { }

        getByName("desktopMain").dependencies {
            implementation(libs.compose.desktop)
            api(libs.kcef)
            implementation(libs.kotlin.coroutines.swing)
        }

        webMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-browser:0.5.0")
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.multiplatform.webview"

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}

fun org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.setUpiOSObserver() {
    val path = projectDir.resolve("src/nativeInterop/cinterop/observer")

    binaries.all {
        linkerOpts("-F $path")
        linkerOpts("-ObjC")
    }

    compilations.getByName("main") {
        cinterops.create("observer") {
            compilerOpts("-F $path")
        }
    }
}

mavenPublishing {
    publishToMavenCentral(true)
    signAllPublications()
}
