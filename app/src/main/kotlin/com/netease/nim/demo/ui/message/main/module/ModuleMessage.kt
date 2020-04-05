package com.netease.nim.demo.ui.message.main.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.message.main.fragment.FragmentMessage
import com.netease.nim.demo.ui.message.main.viewmodel.ViewModelMessage
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc 消息Module
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleMessage {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentMessage

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelMessage::class)
    abstract fun viewModel(viewModel: ViewModelMessage): ViewModel

}