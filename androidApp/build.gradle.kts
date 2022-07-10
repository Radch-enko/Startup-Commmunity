plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("${project.rootDir}/keystore")
            storePassword = "123456"
            keyAlias = "key0"
            keyPassword = "123456"
        }
    }
    compileSdk = 31
    defaultConfig {
        applicationId = "startup.community.android"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        signingConfig = signingConfigs.getByName("release")
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-alpha08"
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.voyager)
    implementation(libs.voyager.androidx)
    implementation(libs.kodein.android)
    implementation(libs.kodein.compose)
    implementation(libs.paging.runtime)
    implementation(libs.napier)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.accompanist)
    implementation(libs.kotlinx.datetime)
}