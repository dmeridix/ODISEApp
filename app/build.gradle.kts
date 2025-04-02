plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) // Plugin de Kotlin
}

android {
    namespace = "com.example.odisea"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.odisea"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8" // Configura Kotlin para usar Java 1.8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Dependencias existentes
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Glide: Biblioteca para cargar imágenes
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // Retrofit: Biblioteca para consumir APIs REST
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
}