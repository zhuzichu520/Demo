package com.netease.nim.demo.ui.login.register.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.login.register.fragment.FragmentRegister
import com.netease.nim.demo.ui.login.register.viewmodel.ViewModelRegister
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc 注册Module
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
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