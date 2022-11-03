package com.ht117.config


object Deps {

    object App {
        const val ID = "com.ht117.app"
        const val minSdk = 21
        const val compileSdk = 33
        const val targetSdk = 33
        const val versionCode = 1
        const val versionName = "1.0"
    }

    object Kt {
//        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kt}"
        const val coAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9"
    }

    object Androidx {
        const val coreKtx = "androidx.core:core-ktx:1.9.0"
        const val appCompat = "androidx.appcompat:appcompat:1.5.1"
        const val constraint = "androidx.constraintlayout:constraintlayout:2.1.4"
        const val navFragment = "androidx.navigation:navigation-fragment-ktx:2.5.3"
        const val navUiKtx = "androidx.navigation:navigation-ui-ktx:2.5.3"
        const val paging = "androidx.paging:paging-runtime:3.1.1"
    }

    object Common {
        const val material = "com.google.android.material:material:1.7.0"
    }

    object Koin {
        const val core = "io.insert-koin:koin-core:3.2.2"
        const val android = "io.insert-koin:koin-android:3.2.2"
    }

    object Networks {
        const val ktxSerializeCore = "org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.20"
        const val ktorCore = "io.ktor:ktor-client-core:2.1.3"
        const val ktorOkhttp = "io.ktor:ktor-client-okhttp:2.1.3"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"
        const val ktorContent = "io.ktor:ktor-client-content-negotiation:2.1.3"
        const val ktorJson = "io.ktor:ktor-serialization-kotlinx-json:2.1.3"

//        const val ktorOk = "io.ktor:ktor-client-okhttp:${Version.ktor}"
//        const val ktorJvm = "io.ktor:ktor-client-serialization-jvm:${Version.ktor}"
        const val logger = "com.squareup.okhttp3:logging-interceptor:4.9.0"
        const val coil = "io.coil-kt:coil:2.2.2"
    }

    object Test {
        const val jUnit4 = "junit:junit:"
        const val ktor = "io.ktor:ktor-client-mock:"
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-test:"
        const val mockk = "io.mockk:mockk:"
    }
}