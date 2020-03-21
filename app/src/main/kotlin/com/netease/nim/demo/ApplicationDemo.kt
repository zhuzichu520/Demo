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
import com.zhuzichu.android.mvvm.Mvvm
import com.zhuzichu.android.shared.crash.CrashConfig
import com.zhuzichu.android.shared.global.AppGlobal
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import jonathanfinerty.once.Once


class ApplicationDemo : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        AppGlobal.init(this)
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