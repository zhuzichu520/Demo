plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

Config.initJenkinsProperties(project)

android {

    compileSdkVersion(Config.compileSdkVersion())

    signingConfigs {
        create("release") {
            keyAlias = Config.keyAlias()
            keyPassword = Config.keyPassword()
            storePassword = Config.storePassword()
            storeFile = file(Config.storeFile())
        }

        getByName("debug") {
            storeFile = file("debug.keystore")
        }
    }

    defaultConfig {
        applicationId = Config.applicationId()
        minSdkVersion(Config.minSdkVersion())
        targetSdkVersion(Config.targetSdkVersion())
        versionCode = Config.versionCode()
        versionName = Config.versionName()
        renderscriptTargetApi = 18
        renderscriptSupportModeEnabled = true
        ndk {
            setAbiFilters(listOf("armeabi-v7a"))
        }
        resValue("string", "app_name_new", Config.appName())
        val fields = Config.getBuildConfigFields()
        fields.forEach {
            buildConfigField(it[0], it[1], it[2])
        }
        manifestPlaceholders.apply {
            put("ic_launcher_new", "@mipmap/ic_launcher")
            put("ic_launcher_round_new", "@mipmap/ic_launcher_round")
            put("NIM_KEY", Config.appKeyNim())
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isShrinkResources = true
            isMinifyEnabled = true
            isZipAlignEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders.apply {
                put("AMAP_KEY", Config.appKeyAmapRelease())
            }
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            manifestPlaceholders.apply {
                put("AMAP_KEY", Config.appKeyAmapDebug())
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    dataBinding {
        isEnabled = true
    }

    androidExtensions {
        isExperimental = true
    }

    sourceSets {
        sourceSets["main"].apply {
            java.srcDir("src/main/kotlin")
        }
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to "*.jar"))
    api(project(path = ":shared"))
    kapt(Kapts.DAGGER_ANDROID_PROCESSOR)
    kapt(Kapts.DAGGER_COMPILER)
    kapt(Kapts.RXHTTP_COMPILER)
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.3")
}