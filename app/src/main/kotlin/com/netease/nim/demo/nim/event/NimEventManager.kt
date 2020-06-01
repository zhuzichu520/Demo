package com.netease.nim.demo.nim.event

import com.hiwitech.android.shared.bus.LiveDataBus
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.AuthServiceObserver
import com.netease.nimlib.sdk.auth.OnlineClient
import com.netease.nimlib.sdk.avchat.AVChatManager
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent
import com.netease.nimlib.sdk.avchat.model.AVChatData
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
                LiveDataBus.post(NimEvent.OnRecentContactEvent(list))
            }
        }

    /**
     * 用户在线监听
     */
    private val observeOnlineStatus =
        Observer { statusCode: StatusCode? ->
            statusCode?.let {
                LiveDataBus.post(NimEvent.OnLineStatusEvent(it))
            }
        }

    /**
     * 多端登录监听
     */
    private val observeOnlineClient =
        Observer { onlineClients: List<OnlineClient>? ->
            onlineClients?.let {
                LiveDataBus.post(NimEvent.OnLienClientEvent(it))
            }
        }

    /**
     * 消息状态监听
     */
    private val observeMessageStatus =
        Observer { message: IMMessage? ->
            message?.let {
                LiveDataBus.post(NimEvent.OnMessageStatusEvent(it))
            }
        }

    /**
     * 消息接受监听
     */
    private val observeReceiveMessage =
        Observer { list: List<IMMessage>? ->
            if (!list.isNullOrEmpty()) {
                LiveDataBus.post(NimEvent.OnReceiveMessageEvent(list))
            }
        }

    /**
     * 附件下载监听
     */
    private val observerAttachmentProgress =
        Observer { attachmentProgress: AttachmentProgress? ->
            attachmentProgress?.let {
                LiveDataBus.post(NimEvent.OnAttachmentProgressEvent(it))
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
                LiveDataBus.post(NimEvent.OnUserInfoUpdateEvent(list))
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
                LiveDataBus.post(NimEvent.OnAddedOrUpdatedFriendsEvent(addedOrUpdatedFriends))
            }
            if (!deletedFriends.isNullOrEmpty()) {
                LiveDataBus.post(NimEvent.OnDeletedFriendsEvent(deletedFriends))
            }
        } as Observer<FriendChangedNotify>

    /**
     * 收到对方蒂娜花
     */
    private val observeInComingCall =
        Observer { data: AVChatData ->
            LiveDataBus.post(NimEvent.OnInComingCallEvent(data))
        }

    /**
     * 通话过程中，收到对方挂断电话
     */
    private val observeHangUpNotification =
        Observer { event: AVChatCommonEvent ->
            LiveDataBus.post(NimEvent.OnHangUpNotificationEvent(event))
        }

    private val observeCalleeAckNotification =
        Observer { event: AVChatCalleeAckEvent ->
            LiveDataBus.post(NimEvent.OnCalleeAckNotificationEvent(event))
        }

    private val observeControlNotification =
        Observer { event: AVChatControlEvent ->
            LiveDataBus.post(NimEvent.OnControlNotificationEvent(event))
        }

    fun registerObserves(register: Boolean) {
        NIMClient.getService(MsgServiceObserve::class.java)
            .observeAttachmentProgress(observerAttachmentProgress, register)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeReceiveMessage(observeReceiveMessage, register)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeMsgStatus(observeMessageStatus, register)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeRecentContact(observerRecentContact, register)

        NIMClient.getService(AuthServiceObserver::class.java)
            .observeOnlineStatus(observeOnlineStatus, register)

        NIMClient.getService(AuthServiceObserver::class.java)
            .observeOtherClients(observeOnlineClient, register)

        NIMClient.getService(UserServiceObserve::class.java)
            .observeUserInfoUpdate(observerUserInfoUpdate, register)

        NIMClient.getService(FriendServiceObserve::class.java)
            .observeFriendChangedNotify(observerFriendChangedNotify, register)

        AVChatManager.getInstance().observeIncomingCall(observeInComingCall, register)

        AVChatManager.getInstance().observeHangUpNotification(observeHangUpNotification, register)

        AVChatManager.getInstance()
            .observeCalleeAckNotification(observeCalleeAckNotification, register)

        AVChatManager.getInstance().observeControlNotification(observeControlNotification, register)
    }

}