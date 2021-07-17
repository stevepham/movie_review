plugins {

    Deps.dataPlugins.forEach {
        id(it)
    }
}

android {
    compileSdkVersion(Deps.App.compileSdk)
    buildToolsVersion(Deps.App.buildTools)

    defaultConfig {
        minSdkVersion(Deps.App.minSdk)
        targetSdkVersion(Deps.App.targetSdk)
        versionCode = Deps.App.versionCode
        versionName = Deps.App.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }
}

dependencies {
    implementation(fileTree(Deps.Common.FileTree))

    api(Deps.Kt.stdLib)
    api(Deps.Kt.coAndroid)
    api(Deps.Androidx.coreKtx)
    api(Deps.Androidx.appCompat)
    api(Deps.Androidx.paging)
    api(Deps.Koin.core)
    api(Deps.Koin.scope)

    implementation(Deps.Networks.ktxSerializeCore)
    implementation(Deps.Networks.ktxSerializeJson)
    implementation(Deps.Networks.ktorOk)
    implementation(Deps.Networks.ktorJvm)
    implementation(Deps.Networks.logger)

    implementation(Deps.Test.ktor)
    testImplementation(Deps.Test.jUnit4)
    testImplementation(Deps.Test.mockk)
    testImplementation(Deps.Test.coroutine)

}