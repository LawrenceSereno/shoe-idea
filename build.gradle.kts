// Root-level build.gradle.kts

plugins {
    kotlin("jvm") version "1.8.21" apply false // Ensure Kotlin version is specified
    id("com.android.application") version "8.8.1" apply false
    id("com.android.library") version "8.8.1" apply false
}

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.8.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21") // Ensure Kotlin plugin is included here
    }
}

allprojects {
    // No need to define repositories here anymore.
    // The repositories are now handled in settings.gradle.kts
}
