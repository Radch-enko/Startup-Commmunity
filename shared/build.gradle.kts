import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    id("com.android.library")
    id("com.google.devtools.ksp") version "1.6.21-1.0.5"
    id("dev.icerock.mobile.multiplatform-resources").version("0.18.0")
    id("com.codingfeline.buildkonfig")
    id("kotlinx-serialization")

    kotlin("multiplatform")
    kotlin("native.cocoapods")
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
                api(libs.multiplatform.paging)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.napier)
                implementation(libs.kodein.di)
                implementation(libs.kotlinx.datetime)
                implementation(libs.bundles.ktor)
                implementation(libs.ktorfit)
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

multiplatformResources {
    multiplatformResourcesPackage = "startup.community.shared"
    iosBaseLocalizationRegion = "en"
}

buildkonfig {
    packageName = "startup.community.shared"

    defaultConfigs {
        buildConfigField(
            STRING,
            "BASE_URL",
            "https://product-hunt-projects.herokuapp.com/"
        )
        buildConfigField(
            STRING,
            "IMAGE_SOURCE",
            "https://ph-files.imgix.net/"
        )
    }
}

val ktorfitVersion = "1.0.0-beta06"

dependencies {
    implementation("org.jetbrains:annotations:22.0.0")
    add("kspCommonMainMetadata", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
    add("kspAndroid", "de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfitVersion")
}