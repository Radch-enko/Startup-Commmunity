import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 31
    defaultConfig {
        applicationId = "com.multi.producthunt.android"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "BASE_URL",
            "\"https://api.producthunt.com/v2/api/graphql\""
        )
        buildConfigField(
            "String",
            "APP_ACCESS_TOKEN",
            "\"${getAccessTokenFromLocalProperties()}\""
        )
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
        kotlinCompilerExtensionVersion = "1.1.0"
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
    implementation(libs.paging.runtime)
    implementation(libs.napier)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.accompanist)
}

fun getAccessTokenFromLocalProperties(): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    return properties.getProperty("accessToken")
}