plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.leegosolutions.vms_host_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.leegosolutions.vms_host_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "1.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // to generate database schema
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
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
    viewBinding {
        enable = true
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Country code picker - https://github.com/hbb20/CountryCodePickerProject
    implementation("com.hbb20:ccp:2.7.3")
    // Biometric
    implementation("androidx.biometric:biometric:1.1.0")
    // Material
    implementation ("com.google.android.material:material:1.13.0-alpha03")
    // Room Database
    val room_version = "2.6.1"
    implementation ("androidx.room:room-runtime:$room_version")
    annotationProcessor ("androidx.room:room-compiler:$room_version")
    // ML Kit - Scan barcodes
    implementation ("com.google.mlkit:barcode-scanning:17.0.2")
    // Camera X
    implementation ("androidx.camera:camera-core:1.1.0")
    implementation ("androidx.camera:camera-camera2:1.1.0")
    implementation ("androidx.camera:camera-lifecycle:1.1.0")
    implementation ("androidx.camera:camera-view:1.1.0")
    // Lottie Animation
    val lottieVersion = "3.4.0"
    implementation ("com.airbnb.android:lottie:$lottieVersion")
    // Okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    // zxing - for QR Code Generation
    val zxingVersion = "4.3.0"
    // https://github.com/journeyapps/zxing-android-embedded
    implementation("com.journeyapps:zxing-android-embedded:$zxingVersion")
    // Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    // Email
    implementation(files("libs\\activation.jar"))
    implementation(files("libs\\additionnal.jar"))
    implementation(files("libs\\mail.jar"))

}