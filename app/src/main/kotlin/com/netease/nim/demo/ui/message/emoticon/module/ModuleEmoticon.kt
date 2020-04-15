package com.netease.nim.demo.ui.message.emoticon.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.message.emoticon.fragment.FragmentEmoticon
import com.netease.nim.demo.ui.message.emoticon.viewmodel.ViewModelEmoticon
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
internal abstract class ModuleEmoticon {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentEmoticon

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelEmoticon::class)
    abstract fun viewModel(viewModel: ViewModelEmoticon): ViewModel

}