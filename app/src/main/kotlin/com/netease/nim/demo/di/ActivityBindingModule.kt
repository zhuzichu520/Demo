package com.netease.nim.demo.di


import com.hiwitech.android.mvvm.di.ActivityScoped
import com.netease.nim.demo.ui.contact.module.ModuleContact
import com.netease.nim.demo.ui.launcher.ActivityLauncher
import com.netease.nim.demo.ui.launcher.module.ModuleLauncher
import com.netease.nim.demo.ui.login.main.module.ModuleLogin
import com.netease.nim.demo.ui.login.register.module.ModuleRegister
import com.netease.nim.demo.ui.main.module.ModuleMain
import com.netease.nim.demo.ui.map.module.ModuleAmap
import com.netease.nim.demo.ui.me.main.module.ModuleMe
import com.netease.nim.demo.ui.message.emoticon.module.ModuleEmoticon
import com.netease.nim.demo.ui.message.main.module.ModuleMessage
import com.netease.nim.demo.ui.message.more.module.ModuleMore
import com.netease.nim.demo.ui.session.module.ModuleSession
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * desc Activity Module
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ModuleLauncher::class,
            ModuleLogin::class,
            ModuleMain::class,
            ModuleMe::class,
            ModuleRegister::class,
            ModuleSession::class,
            ModuleContact::class,
            ModuleMessage::class,
            ModuleMore::class,
            ModuleEmoticon::class,
            ModuleAmap::class
        ]
    )
    internal abstract fun launcherActivity(): ActivityLauncher
}