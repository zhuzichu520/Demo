package com.netease.nim.demo.nim.event

import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.OnlineClient
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent
import com.netease.nimlib.sdk.avchat.model.AVChatData
import com.netease.nimlib.sdk.friend.model.Friend
import com.netease.nimlib.sdk.msg.model.AttachmentProgress
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

/**
 * desc IM事件
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class NimEvent {
    /**
     * 最近会话事件
     */
    data class OnRecentContactEvent(val list: List<RecentContact>)

    /**
     * 用户在线监听
     */
    data class OnLineStatusEvent(val statusCode: StatusCode)

    /**
     * 多端登录监听
     */
    data class OnLienClientEvent(val list: List<OnlineClient>?)

    data class OnMessageStatusEvent(val message: IMMessage)
    data class OnReceiveMessageEvent(val list: List<IMMessage>)
    data class OnAttachmentProgressEvent(val attachment: AttachmentProgress)
    data class OnUserInfoUpdateEvent(val list: List<NimUserInfo>)
    data class OnAddedOrUpdatedFriendsEvent(val list: List<Friend>)
    data class OnDeletedFriendsEvent(val list: List<String>)

    data class OnInComingCallEvent(val data: AVChatData)
    data class OnHangUpNotificationEvent(val event: AVChatCommonEvent)
    data class OnCalleeAckNotificationEvent(val event: AVChatCalleeAckEvent)
    data class OnControlNotificationEvent(val event: AVChatControlEvent)
}