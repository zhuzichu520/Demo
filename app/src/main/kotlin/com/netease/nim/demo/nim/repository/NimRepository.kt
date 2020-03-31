package com.netease.nim.demo.nim.repository

import com.netease.nim.demo.nim.NimRequestCallback
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.team.TeamService
import com.netease.nimlib.sdk.team.model.Team
import com.netease.nimlib.sdk.uinfo.UserService
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import com.zhuzichu.android.shared.ext.createFlowable
import io.reactivex.Flowable

interface NimRepository {
    /**
     * 登录
     * @param loginInfo
     */
    fun login(loginInfo: LoginInfo): Flowable<LoginInfo>

    /**
     * 获取群组
     * @param teamId
     */
    fun getTeamById(teamId: String): Flowable<Team>

    /**
     * 获取用户资料
     * @param accid
     */
    fun getUserInfoById(accid: String): Flowable<NimUserInfo>

}

class NimRepositoryImpl(
    private val teamService: TeamService,
    private val userService: UserService,
    private val authService: AuthService
) : NimRepository {

    override fun login(loginInfo: LoginInfo): Flowable<LoginInfo> {
        return createFlowable {
            authService.login(loginInfo).setCallback(NimRequestCallback(this))
        }
    }

    override fun getTeamById(teamId: String): Flowable<Team> {
        return createFlowable {
            try {
                val team = teamService.queryTeamBlock(teamId)
                onNext(team)
                onComplete()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    override fun getUserInfoById(accid: String): Flowable<NimUserInfo> {
        return createFlowable<List<NimUserInfo>> {
            val userInfo = userService.getUserInfo(accid)
            userInfo?.let {
                onNext(listOf<NimUserInfo>(userInfo))
                onComplete()
            } ?: let {
                userService.fetchUserInfo(listOf(accid)).setCallback(NimRequestCallback(it))
            }
        }.map {
            it[0]
        }
    }


}