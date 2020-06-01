package com.netease.nim.demo.ui.avchat.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.avchat.fragment.FragmentAvchat
import com.netease.nim.demo.ui.avchat.viewmodel.ViewModelAvchat
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/29 11:33 AM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleAvchat {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentAvchat

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelAvchat::class)
    abstract fun viewModel(viewModel: ViewModelAvchat): ViewModel

}