buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
        classpath("com.android.tools.build:gradle:7.2.0")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.11.0")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.6.21")
        classpath ("com.apollographql.apollo3:apollo-gradle-plugin:3.1.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-coroutines/maven")
        maven("https://kotlin.bintray.com/kotlinx")
    }
}