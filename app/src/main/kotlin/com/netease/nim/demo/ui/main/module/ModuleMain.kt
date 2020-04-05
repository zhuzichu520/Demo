package com.netease.nim.demo.ui.main.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.main.fragment.FragmentMain
import com.netease.nim.demo.ui.main.viewmodel.ViewModelMain
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc 主页面Module
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleMain {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentMain

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelMain::class)
    abstract fun viewModel(viewModel: ViewModelMain): ViewModel


}