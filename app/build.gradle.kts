plugins {
    id("kotlin-kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.gamedealshunter"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.gamedealshunter"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

dependencies {
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.datastore)
    implementation(libs.work.runtime)
    implementation(libs.koin.workmanager)
    implementation(libs.retrofit.converter)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
    implementation(libs.coil.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.work.testing)
    testImplementation(libs.test.core)
    testImplementation(libs.room.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}