plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.apollographql.apollo3").version("3.1.0")
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    //iosSimulatorArm64() sure all ios dependencies support this target

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }
    
    sourceSets {
        val commonMain by getting{
            dependencies {
                //Network
                implementation(libs.ktor.core)
                implementation(libs.ktor.logging)
                // GraphQL
                implementation(libs.bundles.apollo)
                // Multiplatform Paging
                implementation(libs.multiplatform.paging)
                //Coroutines
                implementation(libs.kotlinx.coroutines.core)
                //Logger
                implementation(libs.napier)
                //JSON
                implementation(libs.kotlinx.serialization.json)
                //Key-Value storage
                implementation(libs.multiplatform.settings)
                // DI
                implementation(libs.kodein.di)
                // Navigation
                implementation(libs.bundles.voyager)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        //val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            //iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        //val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            //iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 31
    }
}

apollo {
    packageName.set("com.multi.producthunt")
    generateOptionalOperationVariables.set(false)
}