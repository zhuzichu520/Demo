package com.netease.nim.demo.ui.contact.module

import androidx.lifecycle.ViewModel
import com.netease.nim.demo.ui.contact.fragment.FragmentContact
import com.netease.nim.demo.ui.contact.viewmodel.ViewModelContact
import com.hiwitech.android.mvvm.di.ChildFragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

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