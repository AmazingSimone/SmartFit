plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.example.smartfit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.smartfit"
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.espresso.core)
    implementation(libs.firebase.vertexai)
    implementation(libs.firebase.auth)
    implementation(libs.koin.android)
    implementation(libs.koin.android)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.android)
    implementation(libs.koin.android)
    implementation(libs.koin.android)
    implementation(libs.koin.android)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.bluetooth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.androidx.material.icons.extended)


    //Google Sign In
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)


    implementation(libs.firebase.auth.ktx)


    implementation(libs.kmpauth.google.v220) //Google One Tap Sign-In
    implementation(libs.kmpauth.firebase.v220) //Integrated Authentications with Firebase
    implementation(libs.kmpauth.uihelper) //UiHelper SignIn buttons (AppleSignIn, GoogleSignInButton)


    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)


    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.json.v151)

    implementation(libs.coil.compose)


    //qr
    implementation(libs.core)


    //camera
    implementation(libs.guava)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle.v110)
    implementation(libs.androidx.camera.view.v100alpha31)
    
    //permissions
    implementation(libs.accompanist.permissions)




}