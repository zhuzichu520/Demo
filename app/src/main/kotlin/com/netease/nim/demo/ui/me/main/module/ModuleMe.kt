package com.netease.nim.demo.ui.me.main.module

import androidx.lifecycle.ViewModel
import com.netease.nim.demo.ui.me.main.fragment.FragmentMe
import com.netease.nim.demo.ui.me.main.viewmodel.ViewModelMe
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ModuleMe {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentMe

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelMe::class)
    abstract fun viewModel(viewModel: ViewModelMe): ViewModel

}