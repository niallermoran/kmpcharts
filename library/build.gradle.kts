import org.jetbrains.compose.internal.utils.localPropertiesFile
import java.io.FileInputStream
import java.util.Locale
import java.util.Properties
import com.vanniktech.maven.publish.*

plugins {

    id("com.vanniktech.maven.publish") version "0.34.0"

    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)

    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    id("maven-publish")
    id("signing")
}

group = "com.tryingtorun.kmpcharts"
version = libs.versions.kmpcharts.get()

// Load local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
    val keyFile = localProperties.getProperty("signingKeyFile")
    println("local.properties file found: $keyFile")
} else
    println("local.properties file not found")


kotlin {


    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {


        namespace = "com.niallermoran.kmpcharts.library"
        compileSdk = 36
        minSdk = 24

        /*
         withHostTestBuilder {
         }

         *//*withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }*/
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "libraryKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
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
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        /* getByName("androidDeviceTest") {
             dependencies {
                 implementation(libs.androidx.runner)
                 implementation(libs.androidx.core)
                 implementation(libs.androidx.testExt.junit)
             }
         }*/

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}

// <module directory>/build.gradle.kts

mavenPublishing {
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