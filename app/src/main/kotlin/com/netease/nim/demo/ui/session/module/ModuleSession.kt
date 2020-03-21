package com.netease.nim.demo.ui.session.module

import androidx.lifecycle.ViewModel
import com.netease.nim.demo.ui.session.fragment.FragmentSession
import com.netease.nim.demo.ui.session.viewmodel.ViewModelSession
import com.zhuzichu.android.mvvm.di.ChildFragmentScoped
import com.zhuzichu.android.mvvm.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

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