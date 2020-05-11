package com.netease.nim.demo.ui.camera.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.camera.fragment.FragmentCamera
import com.netease.nim.demo.ui.camera.viewmodel.ViewModelCamera
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/9 9:23 PM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleCamera {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentCamera

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelCamera::class)
    abstract fun viewModel(viewModel: ViewModelCamera): ViewModel

}