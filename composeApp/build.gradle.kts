import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "1.9.0" // Usa tu versión de Kotlin
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation("androidx.navigation:navigation-compose:2.7.7")
            implementation("com.squareup.retrofit2:retrofit:2.9.0")
            implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

            implementation ("com.squareup.okhttp3:okhttp:4.9.0")
            implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.6")
            implementation("io.coil-kt:coil-compose:2.0.0")

            implementation("com.auth0:java-jwt:4.4.0")

            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.8.0")
            implementation ("androidx.compose.material:material-icons-extended:1.7.4")

            implementation("io.ktor:ktor-client-android:2.3.5")

            implementation("io.coil-kt:coil-compose:2.4.0")
            implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
            implementation ("androidx.browser:browser:1.8.0")

            //implementation ("com.stripe:stripe-android:22.0.0")
        }
        commonMain.dependencies {
            // Dependencias multiplataforma
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            implementation("dev.icerock.moko:mvvm-core:0.16.1")
            implementation("dev.icerock.moko:mvvm-compose:0.16.1")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            implementation("io.ktor:ktor-client-core:2.3.5")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")

            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")
            implementation("io.ktor:ktor-client-cio:2.3.5") // Cliente HTTP para Desktop
            implementation("media.kamel:kamel-image:0.7.2") // Para imágenes en Desktop

            implementation ("com.squareup.okhttp3:okhttp:4.9.0")
            implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

        }
    }
}

android {
    namespace = "org.example.projects"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.projects"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.lifecycle.livedata.core.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.compose.material)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.example.projects.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.projects"
            packageVersion = "1.0.0"
        }
    }
}
