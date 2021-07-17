plugins {
    Deps.appPlugins.forEach {
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
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(Deps.Common.FileTree))
    implementation(Deps.Androidx.constraint)
    implementation(Deps.Androidx.navFragment)
    implementation(Deps.Androidx.navUiKtx)

    implementation(Deps.Common.material)
    implementation(Deps.Networks.coil)

    implementation(project(":data"))

    testImplementation(Deps.Test.jUnit4)
    testImplementation(Deps.Test.mockk)
    testImplementation(Deps.Test.coroutine)
}