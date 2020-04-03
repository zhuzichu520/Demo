package com.netease.nim.demo.ui.main.module

import androidx.lifecycle.ViewModel
import com.netease.nim.demo.ui.main.fragment.FragmentMain
import com.netease.nim.demo.ui.main.viewmodel.ViewModelMain
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ModuleMain {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentMain

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelMain::class)
    abstract fun viewModel(viewModel: ViewModelMain): ViewModel


}