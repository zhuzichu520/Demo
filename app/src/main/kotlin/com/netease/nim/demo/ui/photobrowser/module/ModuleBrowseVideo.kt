package com.netease.nim.demo.ui.photobrowser.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.photobrowser.fragment.FragmentBrowseVideo
import com.netease.nim.demo.ui.photobrowser.viewmodel.ViewModelBrowseVideo
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
internal abstract class ModuleBrowseVideo {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentBrowseVideo

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelBrowseVideo::class)
    abstract fun viewModel(viewModel: ViewModelBrowseVideo): ViewModel

}