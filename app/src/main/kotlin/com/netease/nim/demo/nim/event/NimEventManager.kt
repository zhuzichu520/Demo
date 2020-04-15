package com.netease.nim.demo.nim.event

import com.hiwitech.android.shared.bus.RxBus
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.AuthServiceObserver
import com.netease.nimlib.sdk.auth.OnlineClient
import com.netease.nimlib.sdk.friend.FriendServiceObserve
import com.netease.nimlib.sdk.friend.model.FriendChangedNotify
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.msg.model.AttachmentProgress
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.uinfo.UserServiceObserve
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

object NimEventManager {

    /**
     * 联系人监听
     */
    private val observerRecentContact =
        Observer { list: List<RecentContact>? ->
            if (!list.isNullOrEmpty()) {
                RxBus.post(NimEvent.OnRecentContactEvent(list))
            }
        }

    /**
     * 用户在线监听
     */
    private val observeOnlineStatus =
        Observer { statusCode: StatusCode? ->
            statusCode?.let {
                RxBus.post(NimEvent.OnLineStatusEvent(it))
            }
        }

    /**
     * 多端登录监听
     */
    private val observeOnlineClient =
        Observer { onlineClients: List<OnlineClient>? ->
            onlineClients?.let {
                RxBus.post(NimEvent.OnLienClientEvent(it))
            }
        }

    /**
     * 消息状态监听
     */
    private val observeMessageStatus =
        Observer { message: IMMessage? ->
            message?.let {
                RxBus.post(NimEvent.OnMessageStatusEvent(it))
            }
        }

    /**
     * 消息接受监听
     */
    private val observeReceiveMessage =
        Observer { list: List<IMMessage>? ->
            if (!list.isNullOrEmpty()) {
                RxBus.post(NimEvent.OnReceiveMessageEvent(list))
            }
        }

    private val observerAttachmentProgress =
        Observer { attachmentProgress: AttachmentProgress? ->
            attachmentProgress?.let {
                RxBus.post(NimEvent.OnAttachmentProgressEvent(it))
            }
        }

    /**
     * 监听用户资料变更  不会实时监听
     * 用户资料除自己之外，不保证其他用户资料实时更新。其他用户数据更新时机为：
     * 1. 调用 fetchUserInfo 方法刷新用户
     * 2. 收到此用户发来消息（如果消息发送者有资料变更，SDK 会负责更新并通知）
     * 3. 程序再次启动，此时会同步好友信息
     */
    private val observerUserInfoUpdate =
        Observer { list: List<NimUserInfo>? ->
            if (!list.isNullOrEmpty()) {
                RxBus.post(NimEvent.OnUserInfoUpdateEvent(list))
            }
        }

    /**
     * 第三方 APP 应在 APP 启动后监听好友关系的变化，当主动添加好友成功、被添加为好友、
     * 主动删除好友成功、被对方解好友关系、好友关系更新、登录同步好友关系数据时都会收到通知。
     */
    private val observerFriendChangedNotify =
        Observer { friendChangedNotify: FriendChangedNotify ->
            val addedOrUpdatedFriends =
                friendChangedNotify.addedOrUpdatedFriends
            val deletedFriends =
                friendChangedNotify.deletedFriends
            if (addedOrUpdatedFriends.isNullOrEmpty()) {
                RxBus.post(NimEvent.OnAddedOrUpdatedFriendsEvent(addedOrUpdatedFriends))
            }
            if (!deletedFriends.isNullOrEmpty()) {
                RxBus.post(NimEvent.OnDeletedFriendsEvent(deletedFriends))
            }
        } as Observer<FriendChangedNotify>


    fun regist() {
        NIMClient.getService(MsgServiceObserve::class.java)
            .observeAttachmentProgress(observerAttachmentProgress, true)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeReceiveMessage(observeReceiveMessage, true)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeMsgStatus(observeMessageStatus, true)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeRecentContact(observerRecentContact, true)

        NIMClient.getService(AuthServiceObserver::class.java)
            .observeOnlineStatus(observeOnlineStatus, true)

        NIMClient.getService(AuthServiceObserver::class.java)
            .observeOtherClients(observeOnlineClient, true)

        NIMClient.getService(UserServiceObserve::class.java)
            .observeUserInfoUpdate(observerUserInfoUpdate, true)

        NIMClient.getService(FriendServiceObserve::class.java)
            .observeFriendChangedNotify(observerFriendChangedNotify, true)

    }

    fun unRegist() {
        NIMClient.getService(MsgServiceObserve::class.java)
            .observeAttachmentProgress(observerAttachmentProgress, false)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeReceiveMessage(observeReceiveMessage, false)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeMsgStatus(observeMessageStatus, false)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeRecentContact(observerRecentContact, false)

        NIMClient.getService(AuthServiceObserver::class.java)
            .observeOnlineStatus(observeOnlineStatus, false)

        NIMClient.getService(AuthServiceObserver::class.java)
            .observeOtherClients(observeOnlineClient, false)

        NIMClient.getService(UserServiceObserve::class.java)
            .observeUserInfoUpdate(observerUserInfoUpdate, false)

        NIMClient.getService(FriendServiceObserve::class.java)
            .observeFriendChangedNotify(observerFriendChangedNotify, false)
    }
}