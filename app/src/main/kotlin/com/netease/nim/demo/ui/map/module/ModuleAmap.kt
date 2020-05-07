package com.netease.nim.demo.ui.map.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.map.fragment.FragmentAmap
import com.netease.nim.demo.ui.map.viewmodel.ViewModelAmap
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/6 10:09 PM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleAmap {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentAmap

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelAmap::class)
    abstract fun viewModel(viewModel: ViewModelAmap): ViewModel

}