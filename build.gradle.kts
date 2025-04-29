plugins {
    kotlin("jvm") version "2.1.0" apply false // Updated to Kotlin 2.1.0
    id("com.android.application") version "8.8.1" apply false
    id("com.android.library") version "8.8.1" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false // Updated Kotlin plugin to 2.1.0
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.8.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0") // Updated Kotlin Gradle plugin to 2.1.0
    }
}
