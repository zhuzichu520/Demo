package com.netease.nim.demo.ui.setting.module

import androidx.lifecycle.ViewModel
import com.hiwitech.android.mvvm.di.FragmentScoped
import com.hiwitech.android.mvvm.di.ViewModelKey
import com.netease.nim.demo.ui.setting.fragment.FragmentSetting
import com.netease.nim.demo.ui.setting.viewmodel.ViewModelSetting
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/1 8:41 PM
 * since: v 1.0.0
 */
@Module
internal abstract class ModuleSetting {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun fragment(): FragmentSetting

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelSetting::class)
    abstract fun viewModel(viewModel: ViewModelSetting): ViewModel

}
