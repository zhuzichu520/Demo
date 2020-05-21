package com.netease.nim.demo

import android.content.Context
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.multidex.MultiDex
import androidx.navigation.AnimBuilder
import com.hiwitech.android.mvvm.Mvvm
import com.hiwitech.android.shared.crash.CrashConfig
import com.hiwitech.android.shared.global.AppGlobal
import com.netease.nim.demo.di.DaggerAppComponent
import com.netease.nim.demo.nim.attachment.NimAttachParser
import com.netease.nim.demo.nim.config.NimSDKOptionConfig
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.util.NIMUtil
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.tencent.smtt.sdk.QbSdk
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import jonathanfinerty.once.Once
import okhttp3.OkHttpClient
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.ssl.SSLSocketFactoryImpl
import rxhttp.wrapper.ssl.X509TrustManagerImpl
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import java.util.concurrent.TimeUnit


/**
 * desc Application
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
class ApplicationDemo : DaggerApplication(), CameraXConfig.Provider {

    override fun onCreate() {
        super.onCreate()
        PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
        AppGlobal.init(this)
        RxHttp.init(getDefaultOkHttpClient(), BuildConfig.DEBUG)
        Once.initialise(this)
        CrashConfig.Builder.create().apply()
        Mvvm.setAnimBuilder(
            AnimBuilder().apply {
                enter = R.anim.h_enter
                exit = R.anim.h_exit
                popEnter = R.anim.h_pop_enter
                popExit = R.anim.h_pop_exit
            }
        )

        NIMClient.init(this, loginInfo(), NimSDKOptionConfig.getSDKOptions(this))

        if (NIMUtil.isMainProcess(this)) {
            //开启数据同步监听
            NIMClient.toggleNotification(NimUserStorage.notifyToggle)
            // 注册自定义消息附件解析器
            NIMClient.getService(MsgService::class.java)
                .registerCustomAttachmentParser(NimAttachParser())
        }

        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {

            override fun onCoreInitFinished() {

            }

            override fun onViewInitFinished(isInit: Boolean) {

            }
        })

    }

    /**
     * 获取默认HttopClient
     */
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

    /**
     * 获取IM登录信息
     */
    private fun loginInfo(): LoginInfo? {
        val account = NimUserStorage.account
        val token = NimUserStorage.token
        if (!account.isNullOrEmpty() && !token.isNullOrEmpty()) {
            return LoginInfo(account, token)
        }
        return null
    }

    /**
     * Dagger
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    /**
     * MultiDex
     */
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}