pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}
enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "Startup Community"
include(":androidApp")
include(":shared")