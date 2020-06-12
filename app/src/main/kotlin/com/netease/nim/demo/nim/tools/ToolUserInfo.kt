package com.netease.nim.demo.nim.tools

import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.friend.FriendService
import com.netease.nimlib.sdk.uinfo.UserService
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

object ToolUserInfo {

    fun getUserInfo(account: String): NimUserInfo {
        return NIMClient.getService(UserService::class.java).getUserInfo(account)
    }

    fun getUserName(account: String): String {
        return getUserInfo(account).name
    }

    fun getAlias(account: String): String? {
        val friend = NIMClient.getService(FriendService::class.java).getFriendByAccount(account)
            ?: return null
        return friend.alias
    }

    fun getUserDisplayName(account: String): String {
        val name = getUserInfo(account).name
        val alias: String? = getAlias(account)
        if (alias.isNullOrEmpty())
            return name
        return alias
    }

}