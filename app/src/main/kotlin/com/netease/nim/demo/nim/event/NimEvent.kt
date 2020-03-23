package com.netease.nim.demo.nim.event

import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.OnlineClient
import com.netease.nimlib.sdk.friend.model.Friend
import com.netease.nimlib.sdk.msg.model.AttachmentProgress
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

class NimEvent {
    data class OnRecentContactEvent(val list: List<RecentContact>)
    data class OnLineStatusEvent(val statusCode: StatusCode)
    data class OnLienClientEvent(val list: List<OnlineClient>)
    data class OnMessageStatusEvent(val message: IMMessage)
    data class OnReceiveMessageEvent(val list: List<IMMessage>)
    data class OnAttachmentProgressEvent(val attachment: AttachmentProgress)
    data class OnUserInfoUpdateEvent(val list:  List<NimUserInfo>)
    data class OnAddedOrUpdatedFriendsEvent(val list: List<Friend>)
    data class OnDeletedFriendsEvent(val list: List<String>)
}