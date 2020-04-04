package com.netease.nim.demo.ui.session.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.session.fragment.FragmentSession
import com.netease.nim.demo.ui.session.viewmodel.ViewModelSession
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc 会话列表Module
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleSession {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentSession

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelSession::class)
    abstract fun viewModel(viewModel: ViewModelSession): ViewModel

}