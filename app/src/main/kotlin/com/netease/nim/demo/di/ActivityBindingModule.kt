package com.netease.nim.demo.di


import com.netease.nim.demo.ui.contact.module.ModuleContact
import com.netease.nim.demo.ui.launcher.ActivityLauncher
import com.netease.nim.demo.ui.launcher.module.ModuleLauncher
import com.netease.nim.demo.ui.login.main.module.ModuleLogin
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
            ModuleLauncher::class,
            ModuleLogin::class,
            ModuleMain::class,
            ModuleSession::class,
            ModuleContact::class
        ]
    )
    internal abstract fun launcherActivity(): ActivityLauncher
}