import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.ksp)
   // alias(libs.plugins.room)
 


}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            export("io.github.mirzemehdi:kmpnotifier:1.5.1")
            isStatic = true
        }
    }

    /*
    val commonMain by sourceSets.getting {
        dependencies {
            api(libs.multiplatform.paging.v311)
        }
    }
     */

    sourceSets {

        iosMain.dependencies {

            implementation(libs.sqldelight.ios)
            implementation(libs.ktor.client.darwin)
            implementation("io.github.mirzemehdi:kmpnotifier:1.5.1")
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.sqldelight.android)
            implementation(libs.ktor.client.android)
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.datetime)

            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.no.arg)


            //firebase
            implementation(libs.firebase.auth)
            implementation(libs.firebase.database)
            implementation(libs.firebase.storage)


            implementation(libs.voyager.navigator)
            // TabNavigator
            implementation(libs.voyager.tab.navigator)

            // Transitions
            implementation(libs.voyager.transitions)
            //coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network.okhttp)


            //picker from media
            implementation(libs.mpfilepicker)
            //play video
            implementation(libs.compose.multiplatform.media.player)

            implementation(libs.decompose)
            implementation(libs.kmp.viewmodel.compose)

            implementation(libs.paging.common)
            implementation(libs.paging.compose.common)


            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            api(libs.kmpnotifier)






        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(kotlin("test-annotations-common"))
            implementation(libs.assertk)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)

            implementation(libs.mockk)
        }

    }
}

android {
    namespace = "com.stevdza_san.demo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.stevdza_san.demo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    ksp(libs.room.compiler)
}


sqldelight {
    databases {
        create("PostDatabase") {
            packageName.set("com.stevdza_san")
        }
    }
}

