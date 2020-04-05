package com.netease.nim.demo.di

import com.hiwitech.android.mvvm.di.ViewModelModule
import com.netease.nim.demo.ApplicationDemo
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * desc AppComponent
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ViewModelModule::class,
        AppModule::class,
        ActivityBindingModule::class,
        NimModule::class
    ]
)

interface AppComponent : AndroidInjector<ApplicationDemo> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: ApplicationDemo): AppComponent
    }
}