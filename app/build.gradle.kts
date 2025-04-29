plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.google.gms.google.services)
}
android {
    namespace = "com.example.recycleviewtesting"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.recycleviewtesting"
        minSdk = 31
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
        buildConfig = false
        viewBinding = true   // ✅ ADD THIS
    }
}


dependencies {
    // RecyclerView for displaying lists of items
    implementation(libs.recyclerview)

    // Glide for image loading
    implementation(libs.glide)

    // Kotlin Standard Library
    implementation(libs.core.ktx)

    // AppCompat for backward compatibility
    implementation(libs.appcompat)

    // Material Components (for UI components like buttons, text fields, etc.)
    implementation(libs.material)  // Keep only one version

    // Activity KTX for lifecycle management and other utility methods
    implementation(libs.activity.ktx)

    // ConstraintLayout for flexible layouts
    implementation(libs.constraintlayout)
    implementation(libs.activity)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0") // Updated Kotlin stdlib
    implementation("com.google.firebase:firebase-auth-ktx:23.2.0") // Ensure Firebase is using the correct version
    implementation("com.google.android.gms:play-services-auth:20.2.0")
    // Unit tests
    testImplementation(libs.junit)

    // Android test dependencies
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
