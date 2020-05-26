package com.netease.nim.demo.ui.dialog.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.dialog.fragment.FragmentOptions
import com.netease.nim.demo.ui.dialog.viewmodel.ViewModelOptions
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc 个人中心module
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleOptions {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentOptions

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelOptions::class)
    abstract fun viewModel(viewModel: ViewModelOptions): ViewModel

}