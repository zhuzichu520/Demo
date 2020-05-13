plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {

    compileSdkVersion(Config.compileSdkVersion())

    defaultConfig {
        minSdkVersion(Config.minSdkVersion())
        targetSdkVersion(Config.targetSdkVersion())
        versionCode = Config.versionCode()
        versionName = Config.versionName()
        consumerProguardFiles("consumer-rules.pro")
    }

    sourceSets {
        getByName("main") {
            res.srcDir("src/main/res")
            jniLibs.srcDir("libs")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
}

dependencies {
    api(fileTree("dir" to "libs", "include" to "*.jar"))
    api(kotlin("stdlib-jdk8"))

    api(SupportLibs.ANDROIDX_APPCOMPAT)
    api(SupportLibs.ANDROIDX_CORE_KTX)
    api(SupportLibs.ANDROIDX_CONSTRAINTLAYOUT)
    api(SupportLibs.ANDROIDX_RECYCLERVIEW)
    api(SupportLibs.ANDROIDX_EXIFINTERFACE)
    api(SupportLibs.ANDROIDX_SWIPEREFRESHLAYOUT)


    kapt(Kapts.DAGGER_COMPILER)
    kapt(Kapts.DAGGER_ANDROID_PROCESSOR)
    kapt(Kapts.GLIDE_COMPILER)

    api(Libs.AUTODISPOSE)
    api(Libs.AUTODISPOSE_ANDROID)
    api(Libs.AUTODISPOSE_ANDROID_ARCHCOMPONENTS)

    api(Libs.DAGGER_ANDROID)
    api(Libs.DAGGER_ANDROID_SUPPORT)

    api(Libs.NAVIGATION_UI_KTX)
    api(Libs.NAVIGATION_FRAGMENT_KTX)

    api(Libs.MATERIAL)

    api(Libs.OKHTTP)
    api(Libs.RXANDROID)
    api(Libs.RXBINDING_CORE)
    api(Libs.RXJAVA)
    api(Libs.RXHTTP)
    api(Libs.RXPERMISSIONS)

    api(Libs.GLIDE_OKHTTP)
    api(Libs.GLIDE)
    api(Libs.GLIDE_TRANSFORMATIONS)

    api(Libs.BINDING_COLLECTION_ADAPTER)
    api(Libs.BINDING_COLLECTION_ADAPTER_RECYCLERVIEW)
    api(Libs.BINDING_COLLECTION_ADAPTER_VIEWPAGER2)

    api(Libs.TIMBER)
    api(Libs.LOGBACK_ANDROID)
    api(Libs.SLF4J)

    api(Libs.FLEXBOX)

    api(Libs.AUTOSZIE)
    api(Libs.ONCE)
    api(Libs.MULTIDEX)
    api(Libs.MMKV)

    api(Libs.AGENTWEB)
    api(Libs.POPUPWINDOW)

    api(Libs.GUAVA)

    api(Libs.FASTJSON)
    api(Libs.LIVEEVENTBUS)
    api(Libs.MATISSE)
    api(Libs.LUBAN)

    api(Libs.PHOTOVIEW)
    api(Libs.IMAGEZOOM)
    api(Libs.SUBSAMPLING)

    api(MyLibs.DEVELOPER_LIBS)
    api(MyLibs.DEVELOPER_MVVM)
    api(MyLibs.DEVELOPER_WIDGET)
}