package com.netease.nim.demo.nim.provider

import android.content.Context
import android.graphics.Bitmap
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.uinfo.UserInfoProvider
import com.netease.nimlib.sdk.uinfo.model.UserInfo

class NimUserInfoProvider(
    val context: Context
) : UserInfoProvider {

    override fun getUserInfo(account: String?): UserInfo? {
        return null
    }

    override fun getAvatarForMessageNotifier(
        sessionType: SessionTypeEnum?,
        sessionId: String?
    ): Bitmap? {
        return null
    }

    override fun getDisplayNameForMessageNotifier(
        account: String?,
        sessionId: String?,
        sessionType: SessionTypeEnum?
    ): String? {
        return null
    }

}