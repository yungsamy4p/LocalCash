plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.localcash"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.localcash"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

    // Librerías base de Android
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // Pilar 1: Autenticación Biométrica
    implementation("androidx.biometric:biometric:1.1.0")
    // Pilar 2: Mapas Offline (osmdroid)
    implementation("org.osmdroid:osmdroid-android:6.1.16")


}