package com.netease.nim.demo.nim.repository

import com.hiwitech.android.shared.ext.createFlowable
import com.netease.nim.demo.nim.NimRequestCallback
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum
import com.netease.nimlib.sdk.team.TeamService
import com.netease.nimlib.sdk.team.model.Team
import com.netease.nimlib.sdk.uinfo.UserService
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import io.reactivex.Flowable

/**
 * desc IM数据仓库
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
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


    /**
     * 获取消息列表
     * @param anchor
     */
    fun getMessageList(anchor: IMMessage, pageSize: Int): Flowable<List<IMMessage>>

    /**
     * 发送消息
     * @param message 消息
     * @param resend 是否重发
     */
    fun sendMessage(message: IMMessage, resend: Boolean): Flowable<Void>
}

class NimRepositoryImpl(
    private val teamService: TeamService,
    private val userService: UserService,
    private val authService: AuthService,
    private val msgService: MsgService
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

    override fun getMessageList(anchor: IMMessage, pageSize: Int): Flowable<List<IMMessage>> {
        return createFlowable {
            msgService.queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD, pageSize, true)
                .setCallback(NimRequestCallback(this))
        }
    }

    override fun sendMessage(message: IMMessage, resend: Boolean): Flowable<Void> {
        return createFlowable {
            msgService.sendMessage(message, resend).setCallback(NimRequestCallback(this))
        }
    }


}