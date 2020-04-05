package com.netease.nim.demo.ui.launcher.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.launcher.fragment.FragmentLauncher
import com.netease.nim.demo.ui.launcher.viewmodel.ViewModelLauncher
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc 启动ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleLauncher {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentLauncher

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelLauncher::class)
    abstract fun viewModel(viewModel: ViewModelLauncher): ViewModel

}