
# Perfect-android-library-template

This is how I prefer my android setup. This setup has a sample app and a deployable library that deploys to maven central once released. It uses detekt and lint and uses github actions to verify code in pull requests

Features:
* Detekt
* Lint
* Deploy to maven central
* Verify PR (github actions)
* Deployment using github releases
* Auto versioning based on GIT tags
* Issue templates
* Funding
* CI configurations (ci-gradle.properties)
* Dependabot
* PR templates
* Release management
* KTS gradle files

## How to use this repo as a template
1. Press "use this template" above or go to https://github.com/warting/Perfect-android-library-template/generate 
1. Make an awesome library
1. Feel free to change from MIT License to whatever you want!
1. Create a sonatype account (https://getstream.io/blog/publishing-libraries-to-mavencentral-2021/)
1. Setup github secrets.
   1. OSSRH_USERNAME
   1. OSSRH_PASSWORD
   1. SONATYPE_STAGING_PROFILE_ID
   1. SIGNING_KEY_ID
   1. SIGNING_PASSWORD
   1. SIGNING_KEY
1. Deploy your lib using github release functionality

## Delete everything above this and change everything below

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/se.warting.perfect-android-library/template/badge.png)](https://maven-badges.herokuapp.com/maven-central/se.warting.se.warting.perfect-android-library/template)

# Title of library

Describe your project here

## How to include in your project

The library is available via MavenCentral:

```
allprojects {
    repositories {
        // ...
        mavenCentral()
    }
}
```

Add it to your module dependencies:

```
dependencies {
    implementation("se.warting.perfect-android-library:template:<latest_version>")
}
```

## How to use

All you need to do is to call `SampleThingie`:

```
    SampleThingie()
```

For a full implementation
see: [Full sample](app/src/main/java/se/warting/perfectandroidlibrarytemplate/MainActivity.kt)

## Notes

Some notes