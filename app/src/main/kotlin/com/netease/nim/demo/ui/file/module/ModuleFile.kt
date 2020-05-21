package com.netease.nim.demo.ui.file.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.file.fragment.FragmentFile
import com.netease.nim.demo.ui.file.viewmodel.ViewModelFile
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleFile {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentFile

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelFile::class)
    abstract fun viewModel(viewModel: ViewModelFile): ViewModel

}