pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}
enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "Product_Hunt"
include(":androidApp")
include(":shared")