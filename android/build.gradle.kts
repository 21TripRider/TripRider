buildscript {
    repositories {
        google()
        mavenCentral()
        // 🔹 Kakao 전용 레포
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.3.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        // 🔹 Kakao 전용 레포
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}
