import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("com.apollographql.apollo3").version("3.1.0")
    id("dev.icerock.mobile.multiplatform-resources").version("0.18.0")
    kotlin("plugin.serialization") version "1.4.32"
    id("com.codingfeline.buildkonfig")
}

version = "1.0"

dependencies {
    commonMainApi(libs.multiplatform.resources)
}

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
        val commonMain by getting {
            dependencies {
                //Network
                implementation(libs.ktor.core)
                implementation(libs.ktor.logging)
                // GraphQL
                implementation(libs.bundles.apollo)
                // Multiplatform Paging
                api(libs.multiplatform.paging)
                //Coroutines
                implementation(libs.kotlinx.coroutines.core)
                //Logger
                implementation(libs.napier)
                // DI
                implementation(libs.kodein.di)
                // Kotlinx Datetime
                implementation(libs.kotlinx.datetime)
                // Ktor
                implementation(libs.bundles.ktor)
                // Kotlinx Serialization
                implementation(libs.bundles.serialization)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.bundles.okhttp)
                implementation(libs.ktor.client.okhttp)
            }
        }
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

multiplatformResources {
    multiplatformResourcesPackage = "com.multi.producthunt"
    iosBaseLocalizationRegion = "en"
}

buildkonfig {
    packageName = "com.multi.producthunt"
    // objectName = "YourAwesomeConfig"
    // exposeObjectWithName = "YourAwesomePublicConfig"

    defaultConfigs {

        buildConfigField(
            STRING,
            "BASE_URL",
            "https://api.producthunt.com/v2/api/graphql"
        )
        buildConfigField(
            STRING,
            "APP_ACCESS_TOKEN",
            getAccessTokenFromLocalProperties()
        )
        buildConfigField(
            STRING,
            "ALGOLIA_SEARCH_URL",
            "https://0h4smabbsg-dsn.algolia.net/1/indexes/"
        )
        buildConfigField(
            STRING,
            "ALGOLIA_TOKEN",
            "9670d2d619b9d07859448d7628eea5f3"
        )
        buildConfigField(
            STRING,
            "ALGOLIA_APP_ID",
            "0H4SMABBSG"
        )
        buildConfigField(
            STRING,
            "IMAGE_SOURCE",
            "https://ph-files.imgix.net/"
        )
    }
}

fun getAccessTokenFromLocalProperties(): String {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    return properties.getProperty("accessToken")
}