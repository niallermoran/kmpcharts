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
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

            }
        }

    }
}

// <module directory>/build.gradle.kts


val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { load(it) }
    }
}

// Apply properties to project
localProperties.forEach { key, value ->
    project.ext.set(key.toString(), value)
}

mavenPublishing {


    signing {

    }
    publishToMavenCentral(
        automaticRelease = true
    )

    signAllPublications()

    coordinates("com.tryingtorun", "kmpcharts", "0.1.0.alpha")

    pom {
        name = "FMP Charts"
        description = "A cross platform library for charts"
        inceptionYear = "2025"
        url = "https://github.com/niallermoran/fmpcharts/"
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
            url = "https://github.com/niallermoran/fmpcharts/"
            connection = "scm:git:git://github.com/niallermoran/fmpcharts.git"
            developerConnection = "scm:git:ssh://git@github.com/niallermoran/fmpcharts.git"
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