package com.netease.nim.demo

import android.content.Context
import androidx.multidex.MultiDex
import androidx.navigation.AnimBuilder
import com.netease.nim.demo.di.DaggerAppComponent
import com.netease.nim.demo.nim.config.NimSDKOptionConfig
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.util.NIMUtil
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.hiwitech.android.mvvm.Mvvm
import com.hiwitech.android.shared.crash.CrashConfig
import com.hiwitech.android.shared.ext.toColorByResId
import com.hiwitech.android.shared.global.AppGlobal
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import jonathanfinerty.once.Once
import okhttp3.OkHttpClient
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.ssl.SSLSocketFactoryImpl
import rxhttp.wrapper.ssl.X509TrustManagerImpl
import java.util.concurrent.TimeUnit


class ApplicationDemo : DaggerApplication() {

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context).apply {
                setPrimaryColor(R.color.color_background_primary.toColorByResId())
                setAccentColor(R.color.color_font_primary.toColorByResId())
            } //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context).apply {
                setPrimaryColor(R.color.color_background_primary.toColorByResId())
                setAccentColor(R.color.color_font_primary.toColorByResId())
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
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
            NIMClient.toggleNotification(NimUserStorage.notifyToggle)
        }
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

    private fun loginInfo(): LoginInfo? {
        val account = NimUserStorage.account
        val token = NimUserStorage.token
        if (!account.isNullOrEmpty() && !token.isNullOrEmpty()) {
            return LoginInfo(account, token)
        }
        return null
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}