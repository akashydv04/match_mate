plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") // For Dagger 2 annotation processing
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "ak.app.matchmate"
    compileSdk = 35

    defaultConfig {
        applicationId = "ak.app.matchmate"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    dependenciesInfo {
        includeInApk = true
        includeInBundle = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)


    // Dagger 2
    implementation(libs.dagger) // Latest stable Dagger 2 version
    kapt(libs.dagger.compiler)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Retrofit for API Integration [cite: 14]
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    // OkHttp for network logging (optional but good for debugging)
    implementation(libs.logging.interceptor)

    // Glide for image loading [cite: 14]
    implementation(libs.glide)
    kapt(libs.compiler)

    // Room for local database [cite: 14]
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx) // Coroutines support

    // RecyclerView [cite: 8]
    implementation(libs.androidx.recyclerview)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

}