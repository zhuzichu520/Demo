package com.netease.nim.demo.ui.message.main.viewmodel

import android.util.LayoutDirection
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.R
import com.netease.nim.demo.nim.tools.ToolUserInfo
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.hiwitech.android.shared.ext.toColorByResId

open class ItemViewModelBaseMessage(
    val message: IMMessage
) {
    val sessionId = message.sessionId
    val name = MutableLiveData<String>()
    val avatar = MutableLiveData<String>()
    val isMe = MutableLiveData<Boolean>()
    val error = MutableLiveData<Int>()
    val placeholder = MutableLiveData<Int>()
    val displayName = MutableLiveData<Int>()
    val textColor = MutableLiveData<Int>()
    val background = MutableLiveData<Int>()
    val layoutDirection = MutableLiveData<Int>()

    init {
        val userInfo = ToolUserInfo.getUserInfo(message.fromAccount)
        isMe.value = isMine()
        avatar.value = userInfo.avatar
        error.value = R.mipmap.nim_avatar_default
        placeholder.value = R.mipmap.nim_avatar_default
        name.value = userInfo.name

        val mine = isMine()

        if (message.sessionType == SessionTypeEnum.Team && !mine) {
            displayName.value = View.VISIBLE
        } else {
            displayName.value = View.GONE
        }


        textColor.value =
            if (mine) R.color.white.toColorByResId() else R.color.color_font_primary.toColorByResId()

        background.value =
            if (mine) R.drawable.skin_messages_right_bubble else R.drawable.skin_messages_left_bubble

        layoutDirection.value =
            if (mine) LayoutDirection.RTL else LayoutDirection.LTR

    }

    private fun isMine(): Boolean {
        return message.fromAccount == NimUserStorage.account
    }
}