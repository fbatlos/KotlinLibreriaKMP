import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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
            implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)

            // Coroutines para manejo asíncrono
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

            // Para MVVM multiplataforma
            implementation("dev.icerock.moko:mvvm-core:0.16.1") // Para ViewModel, LiveData, etc.
            implementation("dev.icerock.moko:mvvm-compose:0.16.1") // Soporte para Compose

            // Serialización
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

            // HTTP (Ktor es más compatible con KMP)
            implementation("io.ktor:ktor-client-core:2.3.5")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")


            /* Retrofit con serialización Kotlin
            implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
            implementation("com.squareup.retrofit2:retrofit:2.9.0")*/
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation("com.squareup.retrofit2:retrofit:2.9.0")
            implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

            implementation ("com.squareup.okhttp3:okhttp:4.9.0")
            implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.6")
            implementation("io.coil-kt:coil-compose:2.0.0")

            implementation("com.auth0:java-jwt:4.4.0")
            implementation("io.ktor:ktor-client-cio:2.3.5")
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
