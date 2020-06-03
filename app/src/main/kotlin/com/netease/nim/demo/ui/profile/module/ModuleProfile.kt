package com.netease.nim.demo.ui.profile.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.profile.fragment.FragmentProfile
import com.netease.nim.demo.ui.profile.viewmodel.ViewModelProfile
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 11:39 AM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleProfile {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentProfile

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelProfile::class)
    abstract fun viewModel(viewModel: ViewModelProfile): ViewModel

}