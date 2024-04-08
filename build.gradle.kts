// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
//    ext {
//        compose_ui_version = '1.2.0'
//    }
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.42")
    }
}
plugins {
    id("com.android.application") version "8.2.0-beta05" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false

}