package com.netease.nim.demo.ui.launcher.module

import androidx.lifecycle.ViewModel
import com.netease.nim.demo.ui.launcher.fragment.FragmentLauncher
import com.netease.nim.demo.ui.launcher.viewmodel.ViewModelLauncher
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ModuleLauncher {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentLauncher

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelLauncher::class)
    abstract fun viewModel(viewModel: ViewModelLauncher): ViewModel

}