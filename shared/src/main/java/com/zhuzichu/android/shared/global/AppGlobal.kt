package com.zhuzichu.android.shared.global

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.core.ImagePipelineConfig
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
        initFresco()
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

    private val MAX_HEAP_SIZE = Runtime.getRuntime().maxMemory().toInt()
    private val MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4
    private const val MAX_DISK_CACHE_SIZE = 40L * ByteConstants.MB
    private fun initFresco() {
        val pipelineConfig = ImagePipelineConfig.newBuilder(context)
            .setBitmapMemoryCacheParamsSupplier {
                MemoryCacheParams(
                    MAX_MEMORY_CACHE_SIZE,
                    Int.MAX_VALUE,
                    MAX_MEMORY_CACHE_SIZE,
                    Int.MAX_VALUE,
                    Int.MAX_VALUE
                )
            }
            .setMainDiskCacheConfig(
                DiskCacheConfig.newBuilder(context)
                    .setBaseDirectoryPath(CacheGlobal.getBaseDiskCacheDir())
                    .setBaseDirectoryName(CacheGlobal.CACHE_FRESCO_FILE_NAME)
                    .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                    .build()
            )
            .setDownsampleEnabled(true)
            .build()
        Fresco.initialize(context, pipelineConfig)
    }

}