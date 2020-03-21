package com.netease.nim.demo.di

import android.content.Context
import com.netease.nim.demo.ApplicationDemo
import com.zhuzichu.android.widget.notify.NotifyManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: ApplicationDemo): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun providesNotifyManager(context: Context): NotifyManager = NotifyManager(context)

}