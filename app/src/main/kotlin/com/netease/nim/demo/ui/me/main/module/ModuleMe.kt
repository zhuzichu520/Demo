package com.netease.nim.demo.ui.me.main.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.me.main.fragment.FragmentMe
import com.netease.nim.demo.ui.me.main.viewmodel.ViewModelMe
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
internal abstract class ModuleMe {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentMe

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelMe::class)
    abstract fun viewModel(viewModel: ViewModelMe): ViewModel

}