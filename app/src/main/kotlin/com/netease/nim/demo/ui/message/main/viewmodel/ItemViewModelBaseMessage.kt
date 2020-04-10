package com.netease.nim.demo.ui.message.main.viewmodel

import android.util.LayoutDirection
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.shared.ext.toColorByResId
import com.netease.nim.demo.R
import com.netease.nim.demo.nim.tools.ToolUserInfo
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 消息Item基类
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
open class ItemViewModelBaseMessage(
    val message: IMMessage
) {
    /**
     * 会话id
     */
    val uuid = message.uuid
    /**
     * 姓名
     */
    val name = MutableLiveData<String>()
    /**
     * 头像
     */
    val avatar = MutableLiveData<String>()
    /**
     * 是否是我的消息
     */
    val isMe = MutableLiveData<Boolean>()
    /**
     * 头像错误图
     */
    val error = MutableLiveData<Int>()
    /**
     * 头像占位图
     */
    val placeholder = MutableLiveData<Int>()
    /**
     * 是否显示姓名
     */
    val displayName = MutableLiveData<Int>()
    /**
     * 字体颜色
     */
    val textColor = MutableLiveData<Int>()
    /**
     * 类容背景
     */
    val background = MutableLiveData<Int>()
    /**
     * 布局方向
     */
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