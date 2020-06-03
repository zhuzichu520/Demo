/**
 * desc
 * author: 朱子楚
 * time: 2020/4/8 2:07 PM
 * since: v 1.0.0
 */

/**
 * 依赖版本
 */
object Versions {
    const val ANDROIDX_TEST_EXT = "1.1.1"
    const val ANDROIDX_TEST = "1.2.0"
    const val APPCOMPAT = "1.1.0"
    const val EXIFINTERFACE = "1.2.0"
    const val RECYCLERVIEW = "1.1.0"
    const val CORE_KTX = "1.2.0"
    const val ESPRESSO_CORE = "3.2.0"
    const val JUNIT = "4.13"
    const val KTLINT = "0.36.0"

    const val DAGGER = "2.27"

    const val OKHTTP = "4.6.0"

    const val RXJAVA = "2.2.19"
    const val RXANDROID = "2.1.1"
    const val RXBINDING = "3.1.0"
    const val RXHTTP = "2.1.1"
    const val RXPERMISSIONS = "0.10.2"

    const val FASTJSON = "1.2.68"

    const val NAVIGATION = "2.2.2"

    const val AUTODISPOSE = "1.4.0"

    const val GLIDE = "4.11.0"
    const val GLIDE_TRANSFORMATIONS = "4.1.0"

    const val MATERIAL = "1.2.0-alpha05"

    const val BINDING_COLLECTION_ADAPTER = "4.0.0"

    const val TIMBER = "4.7.1"
    const val LOGBACK = "2.0.0"
    const val SLF4J = "1.7.30"

    const val SWIPEREFRESHLAYOUT = "1.1.0-beta01"
    const val FLEXBOX = "2.0.1"
    const val CONSTRAINTLAYOUT = "2.0.0-beta4"

    const val MMKV = "1.1.1"
    const val MULTIDEX = "2.0.1"
    const val ONCE = "1.3.0"
    const val AUTOSIZE = "1.2.1"

    const val AGENTWEB = "4.1.3"

    const val GUAVA = "27.0.1-android"

    const val POPUPWINDOW = "2.2.3"

    const val DEVELOPER = "1.2.4"

    const val LIVEEVENTBUS = "1.6.1"

    const val MATISSE = "0.5.3-beta3"
    const val LUBAN = "1.1.8"

    const val PHOTOVIEW = "2.3.0"

    const val CAMERAX = "1.0.0-beta03"
    const val CAMERAX_VIEWA = "1.0.0-alpha10"
    const val CAMERAX_EXTENSIONS = "1.0.0-alpha10"
    const val CAMERAVIEW = "2.6.2"

    const val EXOPLAYER = "2.10.4"

    const val GSYVIDEOPLAYER = "7.1.4"

    const val TBS = "43903"
    const val SONIC = "3.1.0"
    const val RECYCLICAL = "1.1.1"

    const val NIM = "7.7.1"

    const val AMAP = "latest.integration"

    const val TINYPINYIN = "2.0.3"
}

/**
 * 插件版本
 */
object BuildPluginsVersion {
    const val AGP = "3.6.3"
    const val DETEKT = "1.7.4"
    const val KOTLIN = "1.3.72"
    const val KTLINT = "9.2.1"
    const val VERSIONS_PLUGIN = "0.28.0"
    const val ANDROID_MAVEN = "2.1"
}

/**
 * 高德地图
 */
object AmapLibs {
    const val AMAP_3D = "com.amap.api:3dmap:${Versions.AMAP}"
    const val AMAP_LOCATION = "com.amap.api:location:${Versions.AMAP}"
    const val AMAP_SEARCH = "com.amap.api:search:${Versions.AMAP}"
}

/**
 * 云信IM
 */
object NimLibs {
    const val NIM_BASE = "com.netease.nimlib:basesdk:${Versions.NIM}"
    const val NIM_NRTC = "com.netease.nimlib:nrtc:${Versions.NIM}"
    const val NIM_AVCHAT = "com.netease.nimlib:avchat:${Versions.NIM}"
    const val NIM_CHATROOM = "com.netease.nimlib:chatroom:${Versions.NIM}"
    const val NIM_RTS = "com.netease.nimlib:rts:${Versions.NIM}"
    const val NIM_LUCENE = "com.netease.nimlib:lucene:${Versions.NIM}"
    const val NIM_PUSH = "com.netease.nimlib:push:${Versions.NIM}"
}

/**
 * Android基础库
 */
object SupportLibs {
    const val ANDROIDX_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val ANDROIDX_CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    //exifinterface
    const val ANDROIDX_EXIFINTERFACE =
        "androidx.exifinterface:exifinterface:${Versions.EXIFINTERFACE}"
    const val ANDROIDX_RECYCLERVIEW = "androidx.recyclerview:recyclerview:${Versions.RECYCLERVIEW}"
    const val ANDROIDX_CONSTRAINTLAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINTLAYOUT}"
    const val ANDROIDX_SWIPEREFRESHLAYOUT =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.SWIPEREFRESHLAYOUT}"
}

/**
 * 测试库
 */
object TestingLib {
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
}

/**
 * Android测试库
 */
object AndroidTestingLib {
    const val ANDROIDX_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_EXT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
}

/**
 * 第三方其他库
 */
object Libs {

    //dagger
    const val DAGGER_ANDROID = "com.google.dagger:dagger-android:${Versions.DAGGER}"
    const val DAGGER_ANDROID_SUPPORT = "com.google.dagger:dagger-android-support:${Versions.DAGGER}"

    //fastjson
    const val FASTJSON = "com.alibaba:fastjson:${Versions.FASTJSON}"

    //navigation
    const val NAVIGATION_FRAGMENT_KTX =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"

    //autodispose
    const val AUTODISPOSE = "com.uber.autodispose:autodispose:${Versions.AUTODISPOSE}"
    const val AUTODISPOSE_ANDROID =
        "com.uber.autodispose:autodispose-android:${Versions.AUTODISPOSE}"
    const val AUTODISPOSE_ANDROID_ARCHCOMPONENTS =
        "com.uber.autodispose:autodispose-android-archcomponents:${Versions.AUTODISPOSE}"

    //glide
    const val GLIDE_OKHTTP =
        "com.github.bumptech.glide:okhttp3-integration:${Versions.GLIDE}"
    const val GLIDE =
        "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val GLIDE_TRANSFORMATIONS =
        "jp.wasabeef:glide-transformations:${Versions.GLIDE_TRANSFORMATIONS}"

    //material
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"

    //okhttp
    const val OKHTTP =
        "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"

    //rx
    const val RXHTTP = "com.rxjava.rxhttp:rxhttp:${Versions.RXHTTP}"
    const val RXJAVA = "io.reactivex.rxjava2:rxjava:${Versions.RXJAVA}"
    const val RXANDROID = "io.reactivex.rxjava2:rxandroid:${Versions.RXANDROID}"
    const val RXBINDING_CORE = "com.jakewharton.rxbinding3:rxbinding-core:${Versions.RXBINDING}"
    const val RXPERMISSIONS = "com.github.tbruyelle:rxpermissions:${Versions.RXPERMISSIONS}"

    //adapter
    const val BINDING_COLLECTION_ADAPTER =
        "me.tatarka.bindingcollectionadapter2:bindingcollectionadapter:${Versions.BINDING_COLLECTION_ADAPTER}"
    const val BINDING_COLLECTION_ADAPTER_RECYCLERVIEW =
        "me.tatarka.bindingcollectionadapter2:bindingcollectionadapter-recyclerview:${Versions.BINDING_COLLECTION_ADAPTER}"
    const val BINDING_COLLECTION_ADAPTER_VIEWPAGER2 =
        "me.tatarka.bindingcollectionadapter2:bindingcollectionadapter-viewpager2:${Versions.BINDING_COLLECTION_ADAPTER}"

    //log
    const val TIMBER =
        "com.jakewharton.timber:timber:${Versions.TIMBER}"
    const val LOGBACK_ANDROID = "com.github.tony19:logback-android:${Versions.LOGBACK}"
    const val SLF4J = "org.slf4j:slf4j-api:${Versions.SLF4J}"

    //ui
    const val FLEXBOX = "com.google.android:flexbox:${Versions.FLEXBOX}"

    //autosize
    const val AUTOSZIE = "me.jessyan:autosize:${Versions.AUTOSIZE}"
    //once
    const val ONCE = "com.jonathanfinerty.once:once:${Versions.ONCE}"
    //multidex
    const val MULTIDEX = "androidx.multidex:multidex:${Versions.MULTIDEX}"
    //mmkv
    const val MMKV = "com.tencent:mmkv-static:${Versions.MMKV}"

    //guava
    const val GUAVA = "com.google.guava:guava:${Versions.GUAVA}"
    //popupWindow
    const val POPUPWINDOW = "com.github.razerdp:BasePopup:${Versions.POPUPWINDOW}"
    //LiveEventBus
    const val LIVEEVENTBUS = "com.jeremyliao:live-event-bus-x:${Versions.LIVEEVENTBUS}"
    //matisse
    const val MATISSE = "com.zhihu.android:matisse:${Versions.MATISSE}"
    //Luban
    const val LUBAN = "top.zibin:Luban:${Versions.LUBAN}"
    //PhotoView
    const val PHOTOVIEW = "com.github.chrisbanes:PhotoView:${Versions.PHOTOVIEW}"

    const val CAMERAVIEW = "com.otaliastudios:cameraview:${Versions.CAMERAVIEW}"
    const val CAMERAX_CORE = "androidx.camera:camera-core:${Versions.CAMERAX}"
    const val CAMERAX_CAMERA2 = "androidx.camera:camera-camera2:${Versions.CAMERAX}"
    const val CAMERAX_VIEW = "androidx.camera:camera-view:${Versions.CAMERAX_VIEWA}"
    const val CAMERAX_EXTENSIONS =
        "androidx.camera:camera-extensions:${Versions.CAMERAX_EXTENSIONS}"

    const val EXOPLAYER = "com.google.android.exoplayer:exoplayer:${Versions.EXOPLAYER}"

    const val GSYVIDEOPLAYER_JAVA = "com.shuyu:gsyVideoPlayer-java:${Versions.GSYVIDEOPLAYER}"
    const val GSYVIDEOPLAYER_EXO = "com.shuyu:GSYVideoPlayer-exo2:${Versions.GSYVIDEOPLAYER}"

    const val TBS = "com.tencent.tbs.tbssdk:sdk:${Versions.TBS}"
    const val SONIC = "com.tencent.sonic:sdk:${Versions.SONIC}"
    //agentweb
    const val AGENTWEB = "com.just.agentweb:agentweb:${Versions.AGENTWEB}"
    const val AGENTWEB_DOWNLOADER = "com.download.library:Downloader:${Versions.AGENTWEB}"

    const val RECYCLICAL = "com.afollestad:recyclical:${Versions.RECYCLICAL}"

    const val TINYPINYIN = "com.github.promeg:tinypinyin:${Versions.TINYPINYIN}"
}


/**
 * 注解库
 */
object Kapts {
    //dagger
    const val DAGGER_ANDROID_PROCESSOR = "com.google.dagger:dagger-compiler:${Versions.DAGGER}"
    const val DAGGER_COMPILER = "com.google.dagger:dagger-android-processor:${Versions.DAGGER}"

    //glide
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"
    //rx
    const val RXHTTP_COMPILER = "com.rxjava.rxhttp:rxhttp-compiler:${Versions.RXHTTP}"
}

object MyLibs {
    const val DEVELOPER_WIDGET =
        "com.github.zhuzichu520.Developer:widget:${Versions.DEVELOPER}"

    const val DEVELOPER_LIBS =
        "com.github.zhuzichu520.Developer:libs:${Versions.DEVELOPER}"

    const val DEVELOPER_MVVM =
        "com.github.zhuzichu520.Developer:mvvm:${Versions.DEVELOPER}"
}