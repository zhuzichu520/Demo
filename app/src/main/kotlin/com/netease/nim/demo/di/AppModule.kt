package com.netease.nim.demo.di

import android.content.Context
import com.hiwitech.android.widget.notify.NotifyManager
import com.netease.nim.demo.ApplicationDemo
import com.netease.nim.demo.repository.RemoteRepository
import com.netease.nim.demo.repository.RemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * desc AppModule
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Module
class AppModule {

    @Provides
    fun provideContext(application: ApplicationDemo): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun providesNotifyManager(context: Context): NotifyManager = NotifyManager(context)

    @Singleton
    @Provides
    fun providesRemoteRepository(): RemoteRepository = RemoteRepositoryImpl()

}