package com.netease.nim.demo.di

import com.netease.nim.demo.ApplicationDemo
import com.hiwitech.android.mvvm.di.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

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