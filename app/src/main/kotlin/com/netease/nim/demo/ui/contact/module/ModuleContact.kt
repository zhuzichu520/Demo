package com.netease.nim.demo.ui.contact.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.contact.fragment.FragmentContact
import com.netease.nim.demo.ui.contact.viewmodel.ViewModelContact
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc 通讯录Module
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleContact {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentContact

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelContact::class)
    abstract fun viewModel(viewModel: ViewModelContact): ViewModel

}