package com.zhuzichu.android.shared.global

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.tencent.mmkv.MMKV
import com.zhuzichu.android.shared.BuildConfig
import com.zhuzichu.android.shared.log.lumberjack.FileLoggingSetup
import com.zhuzichu.android.shared.log.lumberjack.FileLoggingTree
import com.zhuzichu.android.shared.log.lumberjack.L
import com.zhuzichu.android.shared.theme.ThemeManager
import okhttp3.OkHttpClient
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.ssl.SSLSocketFactoryImpl
import rxhttp.wrapper.ssl.X509TrustManagerImpl
import timber.log.ConsoleTree
import java.util.concurrent.TimeUnit

object AppGlobal {

    private lateinit var application: Application

    val context: Context by lazy {
        application.applicationContext
    }

    fun init(application: Application): AppGlobal {
        AppGlobal.application = application
        CacheGlobal.initDir()
        MMKV.initialize(CacheGlobal.getMmkvCacheDir())
        L.plant(ConsoleTree())
        L.plant(FileLoggingTree(FileLoggingSetup(context).withFolder(CacheGlobal.getLogCacheDir())))
        AppCompatDelegate.setDefaultNightMode(ThemeManager.getNightMode())
        ThemeManager.initTheme(context)
        //或者，调试模式下会有日志输出
        RxHttp.init(getDefaultOkHttpClient(), BuildConfig.DEBUG)
        return this
    }

    private fun getDefaultOkHttpClient(): OkHttpClient {
        val trustAllCert = X509TrustManagerImpl()
        val sslSocketFactory = SSLSocketFactoryImpl(trustAllCert)
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .sslSocketFactory(sslSocketFactory, trustAllCert) //添加信任证书                  
            .build()
    }

}