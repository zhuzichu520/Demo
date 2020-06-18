package com.netease.nim.demo.ui.avchat.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.avchat.fragment.FragmentAvchatVideo
import com.netease.nim.demo.ui.avchat.viewmodel.ViewModelAvchatVideo
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
internal abstract class ModuleAvchatVideo {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentAvchatVideo

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelAvchatVideo::class)
    abstract fun viewModel(viewModel: ViewModelAvchatVideo): ViewModel

}