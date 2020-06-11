package com.netease.nim.demo.nim.repository

import com.google.common.base.Optional
import com.hiwitech.android.shared.ext.createFlowable
import com.netease.nim.demo.nim.NimRequestCallback
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.auth.OnlineClient
import com.netease.nimlib.sdk.friend.FriendService
import com.netease.nimlib.sdk.friend.constant.FriendFieldEnum
import com.netease.nimlib.sdk.friend.constant.VerifyType
import com.netease.nimlib.sdk.friend.model.AddFriendData
import com.netease.nimlib.sdk.friend.model.Friend
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum
import com.netease.nimlib.sdk.msg.model.RecentContact
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
    fun login(loginInfo: LoginInfo): Flowable<Optional<LoginInfo>>

    /**
     * 获取群组
     * @param teamId
     */
    fun getTeamById(teamId: String): Flowable<Team>

    /**
     * 获取用户资料
     * @param accid
     */
    fun getUserInfoById(accid: String): Flowable<Optional<NimUserInfo>>


    /**
     * 获取最近会话列表
     */
    fun getRecentContactList(): Flowable<Optional<List<RecentContact>>>

    /**
     * 获取一个最近会话对象
     * @param contactId 会话id
     * @param sessionTypeEnum 会话类型
     */
    fun getRecentContact(
        contactId: String,
        sessionTypeEnum: SessionTypeEnum
    ): Flowable<Optional<RecentContact>>

    /**
     * 获取消息列表
     * @param anchor
     */
    fun getMessageList(anchor: IMMessage, pageSize: Int): Flowable<Optional<List<IMMessage>>>

    /**
     * 发送消息
     * @param message 消息
     * @param resend 是否重发
     */
    fun sendMessage(message: IMMessage, resend: Boolean): Flowable<Optional<Void>>

    /**
     * 下载附件
     * @param message 附件所在的消息体
     * @param thumb 下载缩略图还是原文件。为true时，仅下载缩略图,该参数仅对图片和视频类消息有效
     */
    fun downloadAttachment(message: IMMessage, thumb: Boolean): Flowable<Optional<Void>>

    /**
     * 获取该消息的会话中所有图片和视频消息
     * @param message 消息
     */
    fun getImageAndVideoMessage(message: IMMessage): Flowable<Optional<List<IMMessage>>>

    /**
     * 获取所有好友用户资料
     */
    fun getFriendInfoList(): Flowable<List<NimUserInfo>>

    /**
     * 多端踢下线
     */
    fun kickOtherOut(client: OnlineClient): Flowable<Optional<Void>>

    /**
     * 根据账号获取好友对象
     */
    fun getFriendByAccount(account: String): Flowable<Optional<Friend>>

    /**
     * 根据账号删除好友
     */
    fun deleteFriend(account: String): Flowable<Optional<Void>>

    /**
     * 根据账号添加好友
     */
    fun addFriend(account: String, verifyType: VerifyType, msg: String?): Flowable<Optional<Void>>

    /**
     * 添加黑名单
     */
    fun addToBlackList(account: String): Flowable<Optional<Void>>

    /**
     * 移除黑名单
     */
    fun removeFromBlackList(account: String): Flowable<Optional<Void>>

    /**
     * p2p消息提醒
     */
    fun setMessageNotify(account: String, isNotify: Boolean): Flowable<Optional<Void>>

    /**
     * 修改好友资料
     */
    fun updateFriendFields(
        account: String,
        fields: Map<FriendFieldEnum, Any>
    ): Flowable<Optional<Void>>
}

class NimRepositoryImpl(
    private val teamService: TeamService,
    private val userService: UserService,
    private val authService: AuthService,
    private val msgService: MsgService,
    private val friendService: FriendService
) : NimRepository {

    override fun login(loginInfo: LoginInfo): Flowable<Optional<LoginInfo>> {
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

    override fun getUserInfoById(accid: String): Flowable<Optional<NimUserInfo>> {
        return createFlowable<Optional<List<NimUserInfo>>> {
            val userInfo = userService.getUserInfo(accid)
            userInfo?.let {
                onNext(Optional.fromNullable(listOf<NimUserInfo>(userInfo)))
                onComplete()
            } ?: let {
                userService.fetchUserInfo(listOf(accid)).setCallback(NimRequestCallback(it))
            }
        }.map {
            Optional.fromNullable(it.get()[0])
        }
    }

    override fun getRecentContactList(): Flowable<Optional<List<RecentContact>>> {
        return createFlowable {
            msgService.queryRecentContacts().setCallback(NimRequestCallback(this))
        }
    }

    override fun getRecentContact(
        contactId: String,
        sessionTypeEnum: SessionTypeEnum
    ): Flowable<Optional<RecentContact>> {
        return createFlowable {
            onNext(Optional.fromNullable(msgService.queryRecentContact(contactId, sessionTypeEnum)))
            onComplete()
        }
    }

    override fun getMessageList(
        anchor: IMMessage,
        pageSize: Int
    ): Flowable<Optional<List<IMMessage>>> {
        return createFlowable {
            msgService.queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD, pageSize, true)
                .setCallback(NimRequestCallback(this))
        }
    }

    override fun sendMessage(message: IMMessage, resend: Boolean): Flowable<Optional<Void>> {
        return createFlowable {
            msgService.sendMessage(message, resend).setCallback(NimRequestCallback(this))
        }
    }

    override fun downloadAttachment(message: IMMessage, thumb: Boolean): Flowable<Optional<Void>> {
        return createFlowable {
            msgService.downloadAttachment(message, thumb).setCallback(NimRequestCallback(this))
        }
    }

    override fun getImageAndVideoMessage(message: IMMessage): Flowable<Optional<List<IMMessage>>> {
        return createFlowable {
            msgService.queryMessageListByTypes(
                listOf(MsgTypeEnum.image, MsgTypeEnum.video),
                MessageBuilder.createEmptyMessage(message.sessionId, message.sessionType, 0),
                0,
                QueryDirectionEnum.QUERY_OLD,
                Int.MAX_VALUE,
                false
            ).setCallback(NimRequestCallback(this))
        }
    }

    override fun getFriendInfoList(): Flowable<List<NimUserInfo>> {
        return createFlowable {
            onNext(userService.getUserInfoList(friendService.friendAccounts))
            onComplete()
        }
    }

    override fun kickOtherOut(client: OnlineClient): Flowable<Optional<Void>> {
        return createFlowable {
            authService.kickOtherClient(client).setCallback(NimRequestCallback(this))
        }
    }

    override fun getFriendByAccount(account: String): Flowable<Optional<Friend>> {
        return createFlowable {
            onNext(Optional.fromNullable(friendService.getFriendByAccount(account)))
            onComplete()
        }
    }

    override fun deleteFriend(account: String): Flowable<Optional<Void>> {
        return createFlowable {
            friendService.deleteFriend(account).setCallback(NimRequestCallback(this))
        }
    }

    override fun addFriend(
        account: String, verifyType: VerifyType, msg: String?
    ): Flowable<Optional<Void>> {
        return createFlowable {
            friendService.addFriend(AddFriendData(account, VerifyType.DIRECT_ADD, msg))
                .setCallback(NimRequestCallback(this))
        }
    }

    override fun addToBlackList(account: String): Flowable<Optional<Void>> {
        return createFlowable {
            friendService.addToBlackList(account).setCallback(NimRequestCallback(this))
        }
    }

    override fun removeFromBlackList(account: String): Flowable<Optional<Void>> {
        return createFlowable {
            friendService.removeFromBlackList(account).setCallback(NimRequestCallback(this))
        }
    }

    override fun setMessageNotify(account: String, isNotify: Boolean): Flowable<Optional<Void>> {
        return createFlowable {
            friendService.setMessageNotify(account, isNotify).setCallback(NimRequestCallback(this))
        }
    }

    override fun updateFriendFields(
        account: String,
        fields: Map<FriendFieldEnum, Any>
    ): Flowable<Optional<Void>> {
        return createFlowable {
            friendService.updateFriendFields(account, fields).setCallback(NimRequestCallback(this))
        }
    }

}