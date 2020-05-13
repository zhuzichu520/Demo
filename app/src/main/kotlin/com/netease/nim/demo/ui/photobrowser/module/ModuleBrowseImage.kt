package com.netease.nim.demo.ui.photobrowser.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.photobrowser.fragment.FragmentBrowseImage
import com.netease.nim.demo.ui.photobrowser.viewmodel.ViewModelBrowseImage
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
internal abstract class ModuleBrowseImage {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentBrowseImage

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelBrowseImage::class)
    abstract fun viewModel(viewModel: ViewModelBrowseImage): ViewModel

}