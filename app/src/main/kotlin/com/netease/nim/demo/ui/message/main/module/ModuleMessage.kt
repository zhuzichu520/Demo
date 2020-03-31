package com.netease.nim.demo.ui.message.main.module

import androidx.lifecycle.ViewModel
import com.netease.nim.demo.ui.message.main.fragment.FragmentMessage
import com.netease.nim.demo.ui.message.main.viewmodel.ViewModelMessage
import com.zhuzichu.android.mvvm.di.FragmentScoped
import com.zhuzichu.android.mvvm.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class ModuleMessage {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentMessage

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelMessage::class)
    abstract fun viewModel(viewModel: ViewModelMessage): ViewModel

}