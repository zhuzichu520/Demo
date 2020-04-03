package com.netease.nim.demo.di


import com.hiwitech.android.mvvm.di.ActivityScoped
import com.netease.nim.demo.ui.contact.module.ModuleContact
import com.netease.nim.demo.ui.launcher.ActivityLauncher
import com.netease.nim.demo.ui.launcher.module.ModuleLauncher
import com.netease.nim.demo.ui.login.main.module.ModuleLogin
import com.netease.nim.demo.ui.login.register.module.ModuleRegister
import com.netease.nim.demo.ui.main.module.ModuleMain
import com.netease.nim.demo.ui.me.main.module.ModuleMe
import com.netease.nim.demo.ui.message.main.module.ModuleMessage
import com.netease.nim.demo.ui.session.module.ModuleSession
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
            ModuleContact::class,
            ModuleMe::class,
            ModuleRegister::class,
            ModuleMessage::class
        ]
    )
    internal abstract fun launcherActivity(): ActivityLauncher
}