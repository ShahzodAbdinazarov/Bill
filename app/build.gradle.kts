plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    id ("dagger.hilt.android.plugin")
    id ("org.jetbrains.kotlin.android")
}

android {
    namespace ="org.hamroh.hisob"
    compileSdk =34

    defaultConfig {
        applicationId ="org.hamroh.hisob"
        minSdk =24
        targetSdk =34
        versionCode =19
        versionName ="1.1.9"

        testInstrumentationRunner="androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled= false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            applicationIdSuffix = ".debug"
            resValue("string", "app_name", "Hisob (DEBUG)")
        }

    }
    compileOptions {
        sourceCompatibility =JavaVersion.VERSION_17
        targetCompatibility =JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation ("androidx.core:core-ktx:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    // Navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Room dependencies
    implementation ("androidx.room:room-runtime:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    ksp("com.google.dagger:hilt-android-compiler:2.49")

    // Shimmer
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

}