package com.netease.nim.demo.di

import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nim.demo.nim.repository.NimRepositoryImpl
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.team.TeamService
import com.netease.nimlib.sdk.uinfo.UserService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * desc IM Module
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */

@Module
class NimModule {

    @Singleton
    @Provides
    fun providesAuthService(): AuthService = NIMClient.getService(AuthService::class.java)

    @Singleton
    @Provides
    fun providesMsgService(): MsgService = NIMClient.getService(MsgService::class.java)

    @Singleton
    @Provides
    fun providesTeamService(): TeamService = NIMClient.getService(TeamService::class.java)

    @Singleton
    @Provides
    fun providesUserService(): UserService = NIMClient.getService(UserService::class.java)

    @Singleton
    @Provides
    fun providesNimRepository(
        teamService: TeamService,
        userService: UserService,
        authService: AuthService,
        msgService: MsgService
    ): NimRepository =
        NimRepositoryImpl(teamService, userService, authService,msgService)

}