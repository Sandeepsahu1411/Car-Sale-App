plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.example.salecar"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.salecar"
        minSdk = 24
        targetSdk = 35
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
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

//google font
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.7")

//splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")
//Icons
    implementation("androidx.compose.material:material-icons-extended-android:1.7.5")

// navigation
    implementation("androidx.navigation:navigation-compose:2.8.3")

//serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

//Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

//retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

//current date time
    implementation ("com.jakewharton.threetenabp:threetenabp:1.4.4")

// preferences data store
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

//coil (imageViewer)
    implementation("io.coil-kt:coil-compose:2.2.2")

//lottie Animation
    implementation("com.airbnb.android:lottie-compose:6.1.0")

// google map

    implementation("com.google.maps.android:maps-compose:6.2.1")



}