package com.netease.nim.demo.ui.permissions.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.permissions.fragment.FragmentPermissions
import com.netease.nim.demo.ui.permissions.viewmodel.ViewModelPermissions
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/7 5:55 PM
 * since: v 1.0.0
 */
@Module
internal abstract class ModulePermissions {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentPermissions

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelPermissions::class)
    abstract fun viewModel(viewModel: ViewModelPermissions): ViewModel

}