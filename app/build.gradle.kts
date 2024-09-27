plugins {

    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.nqobza.wertutors"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nqobza.wertutors"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
    }
    buildFeatures {
        viewBinding = true
    }
    buildFeatures{
        dataBinding = true
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
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))

    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("com.google.android.material:material:1.5.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.3")
//
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
//
    implementation("com.google.firebase:firebase-analytics")

    implementation ("com.google.android.material:material:1.10.0")
    implementation ("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.fragment:fragment-ktx:1.8.2")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.cardview)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.preference)
    implementation(libs.firebase.database.ktx)
    implementation (libs.firebase.auth)
    implementation (libs.firebase.database.vlatestversion)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}