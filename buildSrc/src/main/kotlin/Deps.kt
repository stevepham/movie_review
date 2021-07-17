object Deps {
    val classPaths = listOf(
        "com.android.tools.build:gradle:${Version.buildGradle}",
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kt}",
        "org.jetbrains.kotlin:kotlin-serialization:${Version.serializeCore}"
    )

    val appPlugins = listOf(
        "com.android.application",
        "kotlin-android",
        "kotlin-parcelize"
    )

    val dataPlugins = listOf(
        "com.android.library",
        "kotlin-android",
        "kotlin-kapt",
        "kotlinx-serialization"
    )

    object App {
        const val id = "com.ht117.backbase"
        const val minSdk = 21
        const val compileSdk = 29
        const val targetSdk = 29
        const val buildTools = "29.0.3"
        const val versionCode = 1
        const val versionName = "1.0"
    }

    object Kt {
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kt}"
        const val coAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutine}"
    }

    object Androidx {
        const val coreKtx = "androidx.core:core-ktx:${Version.ktxCore}"
        const val appCompat = "androidx.appcompat:appcompat:${Version.appCompat}"
        const val constraint = "androidx.constraintlayout:constraintlayout:${Version.constraint}"
        const val navFragment = "androidx.navigation:navigation-fragment-ktx:${Version.navFragment}"
        const val navUiKtx = "androidx.navigation:navigation-ui-ktx:${Version.navUiKtx}"
        const val paging = "androidx.paging:paging-runtime:${Version.paging}"
    }

    object Common {
        val FileTree = mapOf("dir" to "libs", "include" to listOf("*.jar"))
        const val material = "com.google.android.material:material:${Version.material}"
    }

    object Koin {
        const val core = "io.insert-koin:koin-core:${Version.koin}"
        const val scope = "io.insert-koin:koin-android:${Version.koin}"
    }

    object Networks {
        const val ktxSerializeCore = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Version.serialize}"
        const val ktxSerializeJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.serialize}"
        const val ktorOk = "io.ktor:ktor-client-okhttp:${Version.ktor}"
        const val ktorJvm = "io.ktor:ktor-client-serialization-jvm:${Version.ktor}"
        const val logger = "com.squareup.okhttp3:logging-interceptor:${Version.okLogger}"
        const val coil = "io.coil-kt:coil:${Version.coil}"
    }

    object Test {
        const val jUnit4 = "junit:junit:${Version.jUnit}"
        const val ktor = "io.ktor:ktor-client-mock:${Version.ktor}"
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.kt}"
        const val mockk = "io.mockk:mockk:${Version.mockk}"
    }
}