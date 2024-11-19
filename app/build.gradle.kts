plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    alias(libs.plugins.googleFirebaseCrashlytics)
}

android {
    namespace = "com.ratjatji.eskhathinitutors"
    compileSdk = 34

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
    }
        defaultConfig {
            applicationId = "com.ratjatji.eskhathinitutors"
            minSdk = 24
            targetSdk = 34
            versionCode = 1
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        buildFeatures {
            viewBinding = true
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

    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation ("com.diogobernardino:williamchart:3.10.1")
        implementation ("androidx.biometric:biometric:1.1.0")

    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    implementation (libs.material.v150)

    implementation (libs.androidx.constraintlayout.v213)

        implementation ("com.applandeo:material-calendar-view:1.9.2")
    implementation("com.google.firebase:firebase-analytics")

    implementation ("com.google.android.material:material:1.10.0")

    implementation ("androidx.drawerlayout:drawerlayout:1.2.0")

    implementation ("androidx.appcompat:appcompat:1.6.1")

    implementation ("androidx.fragment:fragment-ktx:1.8.2")
//
    implementation ("androidx.core:core:1.7.0")
    implementation ("androidx.work:work-runtime-ktx:2.7.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
        // Kotlin Coroutines
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
        implementation ("com.squareup.okhttp3:okhttp:4.9.1")
        implementation ("com.itextpdf:itext7-core:7.1.16")
        implementation ("com.itextpdf:itext7-core:7.1.15")
        implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.generativeai)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.compiler)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.functions)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("com.google.firebase:firebase-auth:latest_version")
    implementation ("com.google.firebase:firebase-database:latest_version")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
}