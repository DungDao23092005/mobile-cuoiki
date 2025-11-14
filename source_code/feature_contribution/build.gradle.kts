plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)

}

android {
    namespace = "com.stushare.feature_contribution" // sửa theo package của bạn
    compileSdk = 36

    defaultConfig {
        applicationId = "com.stushare.feature_contribution" // chỉ cần nếu là module app
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            // proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true // bật nếu dùng viewBinding
        // dataBinding = true // bật nếu cần
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10") // chỉnh version Kotlin nếu cần
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")

    // test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.fragment:fragment-ktx:1.8.1")

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    //CLOUD FIRESTORE
    implementation(libs.firebase.firestore)
}
