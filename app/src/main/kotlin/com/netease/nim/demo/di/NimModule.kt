package com.netease.nim.demo.di

import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NimModule {

    @Singleton
    @Provides
    fun providesAuthService(): AuthService = NIMClient.getService(AuthService::class.java)

}