package com.netease.nim.demo.di


import com.netease.nim.demo.ui.contact.module.ModuleContact
import com.netease.nim.demo.ui.launcher.ActivityLauncher
import com.netease.nim.demo.ui.launcher.module.ModuleLauncher
import com.netease.nim.demo.ui.login.ActivityLogin
import com.netease.nim.demo.ui.login.main.module.ModuleLogin
import com.netease.nim.demo.ui.main.ActivityMain
import com.netease.nim.demo.ui.main.module.ModuleMain
import com.netease.nim.demo.ui.session.module.ModuleSession
import com.zhuzichu.android.mvvm.di.ActivityScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleMain::class,
            ModuleSession::class,
            ModuleContact::class
        ]
    )
    internal abstract fun mainActivity(): ActivityMain

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleLogin::class
        ]
    )
    internal abstract fun loginActivity(): ActivityLogin

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleLauncher::class
        ]
    )
    internal abstract fun launcherActivity(): ActivityLauncher
}