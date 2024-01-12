plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
    id("signing")
    id("org.jetbrains.dokka") version "1.9.10"
}


val PUBLISH_GROUP_ID: String by extra("se.warting.billy")
val PUBLISH_VERSION: String by extra(rootProject.version as String)
val PUBLISH_ARTIFACT_ID by extra("flow")

apply(from = "${rootProject.projectDir}/gradle/publish-module.gradle")

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = false
        compose = false
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs = listOfNotNull(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xskip-prerelease-check"
        )
    }

    lint {
        baseline = file("lint-baseline.xml")
        checkReleaseBuilds = true
        checkAllWarnings = true
        warningsAsErrors = true
        abortOnError = true
        disable.add("LintBaseline")
        disable.add("GradleDependency")
        checkDependencies = true
        checkGeneratedSources = false
        sarifOutput = file("../lint-results-app.sarif")
    }
    namespace = "se.warting.billy.flow"
}

kotlin {
    // https://kotlinlang.org/docs/whatsnew14.html#explicit-api-mode-for-library-authors
    explicitApi()
}


dependencies {

    api("androidx.startup:startup-runtime:1.1.1")

    val billingVersion = "6.0.1"
    api("com.android.billingclient:billing-ktx:$billingVersion")

    implementation("androidx.annotation:annotation:1.6.0")

    val lifecycle_version = "2.6.1"

    implementation("androidx.lifecycle:lifecycle-process:$lifecycle_version")

    // val composeVersion = "1.0.3"
    // implementation("androidx.compose.runtime:runtime:$composeVersion")
    // implementation("androidx.compose.ui:ui:$composeVersion")

    // val coroutineVersion = "1.5.2"
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    // implementation("androidx.core:core-ktx:1.6.10")
    // implementation("androidx.appcompat:appcompat:1.3.1")
    // implementation("com.google.android.material:material:1.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}