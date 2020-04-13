package com.netease.nim.demo.ui.message.more.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.message.more.fragment.FragmentMore
import com.netease.nim.demo.ui.message.more.viewmodel.ViewModelMore
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/13 2:45 PM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleMore {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentMore

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelMore::class)
    abstract fun viewModel(viewModel: ViewModelMore): ViewModel

}