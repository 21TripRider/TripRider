plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")  // settings.gradle.kts와 동일하게 맞춤
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.example.triprider"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = "27.0.12077973"

    defaultConfig {
        applicationId = "com.example.triprider"
        minSdk = 23 // 카카오 SDK 최소 버전 23
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

flutter {
    source = "../.."
}

dependencies {
    implementation("com.kakao.sdk:v2-user:2.21.6")
    implementation("com.kakao.sdk:v2-auth:2.21.6")
}

