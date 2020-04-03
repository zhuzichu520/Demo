package com.netease.nim.demo.ui.login.register.module

import androidx.lifecycle.ViewModel
import com.netease.nim.demo.ui.login.register.fragment.FragmentRegister
import com.netease.nim.demo.ui.login.register.viewmodel.ViewModelRegister
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ModuleRegister {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentRegister

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelRegister::class)
    abstract fun viewModel(viewModel: ViewModelRegister): ViewModel

}