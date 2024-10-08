plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.stockdatawatcher"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.stockdatawatcher"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.tasks)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    //testImplementation("org.mockito:mockito-inline:")  // includes "core"
    //testImplementation("org.mockito:mockito-junit-jupiter:")
    testImplementation("org.mockito:mockito-core:5.13.0")
    androidTestImplementation("org.mockito:mockito-android:5.13.0")
}