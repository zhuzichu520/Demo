package com.netease.nim.demo.nim.tools

import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.uinfo.UserService
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

object ToolUserInfo {

    fun getUserInfo(contactId: String): NimUserInfo {
        return NIMClient.getService(UserService::class.java).getUserInfo(contactId)
    }

}