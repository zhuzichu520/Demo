package com.netease.nim.demo.nim.tools

import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.friend.FriendService
import com.netease.nimlib.sdk.uinfo.UserService
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

object ToolUserInfo {

    fun getUserInfo(contactId: String): NimUserInfo {
        return NIMClient.getService(UserService::class.java).getUserInfo(contactId)
    }

    fun getUserDisplayName(account: String): String {
        val name = getUserInfo(account).name
        val friend = NIMClient.getService(FriendService::class.java).getFriendByAccount(account)
            ?: return name
        val alias: String? = friend.alias
        if (alias.isNullOrEmpty())
            return name
        return alias
    }

}