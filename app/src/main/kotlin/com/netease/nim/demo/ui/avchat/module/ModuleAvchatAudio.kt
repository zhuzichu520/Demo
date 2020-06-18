package com.netease.nim.demo.ui.avchat.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.avchat.fragment.FragmentAvchatAudio
import com.netease.nim.demo.ui.avchat.viewmodel.ViewModelAvchatAudio
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
internal abstract class ModuleAvchatAudio {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentAvchatAudio

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelAvchatAudio::class)
    abstract fun viewModel(viewModel: ViewModelAvchatAudio): ViewModel

}