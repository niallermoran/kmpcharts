@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Properties

plugins {

    id("com.vanniktech.maven.publish") version "0.34.0"

   // alias(libs.plugins.androidKotlinMultiplatformLibrary)

    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)


    id("maven-publish")
    id("signing")
}

group = "com.tryingtorun.kmpcharts"
version = libs.versions.kmpcharts.get()



kotlin {


    // Make sure you have all the targets your consuming app needs
    androidTarget {
        publishLibraryVariants("release")
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.library()
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)

                implementation(libs.kotlin.stdlib)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtimeCompose)

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
               // implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")
            }
        }

    }
}

// <module directory>/build.gradle.kts

mavenPublishing {

    version = libs.versions.kmpcharts.get()

    publishToMavenCentral(
        automaticRelease = false
    )

    signAllPublications()

    coordinates("com.tryingtorun", "kmpcharts", version as String?)

    pom {
        name = "KMP Charts"
        description = "A cross platform library for charts"
        inceptionYear = "2025"
        url = "https://github.com/niallermoran/kmpcharts/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "niallermoran"
                name = "Niall Moran"
                url = "https://github.com/niallermoran/"
            }
        }
        scm {
            url = "https://github.com/niallermoran/kmpcharts/"
            connection = "scm:git:git://github.com/niallermoran/kmpcharts.git"
            developerConnection = "scm:git:ssh://git@github.com/niallermoran/kmpcharts.git"
        }
    }
}

android {
    namespace = "com.tryingtorun.kmpcharts"
    compileSdk = 36  // Add this line

    defaultConfig {
        minSdk = 24
        // Remove targetSdk for libraries (not needed)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}