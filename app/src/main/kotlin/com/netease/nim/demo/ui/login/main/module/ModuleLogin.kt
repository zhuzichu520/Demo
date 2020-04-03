package com.netease.nim.demo.ui.login.main.module

import androidx.lifecycle.ViewModel
import com.netease.nim.demo.ui.login.main.fragment.FragmentLogin
import com.netease.nim.demo.ui.login.main.viewmodel.ViewModelLogin
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ModuleLogin {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentLogin

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelLogin::class)
    abstract fun viewModel(viewModel: ViewModelLogin): ViewModel

}