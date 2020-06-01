package com.netease.nim.demo.ui.theme.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.theme.fragment.FragmentTheme
import com.netease.nim.demo.ui.theme.viewmodel.ViewModelTheme
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ModuleTheme {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentTheme

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelTheme::class)
    abstract fun viewModel(viewModel: ViewModelTheme): ViewModel
}
