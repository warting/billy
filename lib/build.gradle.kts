plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
    id("signing")
    id("org.jetbrains.dokka") version "1.5.31"
    id("com.gladed.androidgitversion") version "0.4.14"
}


androidGitVersion {
    tagPattern = "^v[0-9]+.*"
}


val PUBLISH_GROUP_ID: String by extra("se.warting.billy")
val PUBLISH_VERSION: String by extra(androidGitVersion.name().replace("v", ""))
val PUBLISH_ARTIFACT_ID by extra("flow")

apply(from = "${rootProject.projectDir}/gradle/publish-module.gradle")

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = false
        compose = false
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = listOfNotNull(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xskip-prerelease-check"
        )
    }

    lint {
        lintConfig = file("$rootDir/config/lint/lint.xml")
        lintConfig = file("$rootDir/config/lint/lint.xml")
        //baseline(file("lint-baseline.xml"))
    }
}

kotlin {
    // https://kotlinlang.org/docs/whatsnew14.html#explicit-api-mode-for-library-authors
    explicitApi()
}


dependencies {

    api("androidx.startup:startup-runtime:1.1.0")

    val billingVersion = "4.0.0"
    api("com.android.billingclient:billing-ktx:$billingVersion")

    implementation("androidx.annotation:annotation:1.2.0")
    // To use the Java-compatible @Experimental API annotation
    implementation("androidx.annotation:annotation-experimental:1.1.0")

    val lifecycle_version = "2.4.0-rc01"

    implementation("androidx.lifecycle:lifecycle-process:$lifecycle_version")

    // val composeVersion = "1.0.3"
    // implementation("androidx.compose.runtime:runtime:$composeVersion")
    // implementation("androidx.compose.ui:ui:$composeVersion")

    // val coroutineVersion = "1.5.2"
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutineVersion")

    // implementation("androidx.core:core-ktx:1.6.0")
    // implementation("androidx.appcompat:appcompat:1.3.1")
    // implementation("com.google.android.material:material:1.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}