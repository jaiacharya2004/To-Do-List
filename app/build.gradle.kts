plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services") version "4.4.2"
}

android {
    namespace = "com.example.to_dolist"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.to_dolist"
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
        compose = true
    }
}

dependencies {
    // Firebase implementation
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.android.gms:play-services-auth:21.3.0")
    implementation ("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation ("com.google.android.gms:play-services-maps:19.0.0")
    implementation ("com.google.firebase:firebase-firestore:25.1.1") // Firestore SDK
    implementation ("com.google.android.gms:play-services-base:18.5.0")  // Base Google Play services
    implementation ("com.google.firebase:firebase-messaging:24.1.0")  // For notifications (if needed for alarm/notification)








    implementation ("androidx.room:room-runtime:2.6.1")
    implementation(libs.androidx.media3.database)
    implementation(libs.androidx.runtime.livedata)








    implementation ("androidx.appcompat:appcompat:1.7.0")

    implementation ("androidx.preference:preference-ktx:1.2.1")  // Latest stable version



    // Jetpack Compose dependencies
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.ui:ui:1.7.6")
    implementation("androidx.compose.ui:ui-graphics:1.7.6")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.5")

    implementation(libs.androidx.material3)
    implementation ("androidx.navigation:navigation-compose:2.8.5")

    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    implementation(libs.coil.compose)
    implementation (libs.coil.gif)// GIF support

    implementation ("io.coil-kt:coil-compose:2.5.0")






    // AndroidX libraries
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.process)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.6")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.6")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.6")

}
