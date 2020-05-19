package com.netease.nim.demo.ui.photobrowser.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.photobrowser.fragment.FragmentBrowseImageGif
import com.netease.nim.demo.ui.photobrowser.viewmodel.ViewModelBrowseImageGif
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/11 10:39 AM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleBrowseImageGif {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentBrowseImageGif

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelBrowseImageGif::class)
    abstract fun viewModel(viewModel: ViewModelBrowseImageGif): ViewModel

}