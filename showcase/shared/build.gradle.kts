import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.android.library)
    // kotlin("native.cocoapods")
}

kotlin {
    androidTarget()
    jvm(name = "desktop")
    js(compiler = IR) {
        browser()
        nodejs()
        binaries.executable()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    // Remember to uncomment kotlin("native.cocoapods") above as well
    // iosX64()
    // iosArm64()
    // iosSimulatorArm64()
    // cocoapods {
    //     version = "1.0.0"
    //     summary = "Some description for the Shared Module"
    //     homepage = "Link to the Shared Module homepage"
    //     ios.deploymentTarget = "14.1"
    //     podfile = project.file("../iosApp/Podfile")
    //     framework {
    //         baseName = "shared"
    //         isStatic = true
    //     }
    //     extraSpecAttributes["resources"] = "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    // }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            // implementation("ir.mahozad.multiplatform:wavy-slider:1.0.0-rc")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            // api("ir.mahozad.multiplatform:comshot:x.y.z")
            api(project(":comshot"))
        }
        androidMain.dependencies {
            api(libs.androidx.activity.compose)
            api(libs.androidx.appcompat)
            api(libs.androidx.core.ktx)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
            }
        }
        val jsMain by getting
        val wasmJsMain by getting
        // See above
        // val iosX64Main by getting
        // val iosArm64Main by getting
        // val iosSimulatorArm64Main by getting
    }
}

android {
    namespace = "showcase"
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.java.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.java.get())
    }
    kotlin {
        jvmToolchain(libs.versions.java.get().toInt())
    }
}
