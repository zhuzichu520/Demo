package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.netease.nim.demo.nim.tools.TeamNotificationHelper
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 通知消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ItemViewModelNotificationMessage(
    viewModel: BaseViewModel<*>,
    message: IMMessage,
    contactId: String,
    sessionType: Int
) : ItemViewModelBaseMessage(viewModel, message) {

    private val attachment = message.attachment as NotificationAttachment

    val text = MutableLiveData<String>().apply {
        val fromAccount = message.fromAccount
        value = if (sessionType == SessionTypeEnum.Team.value) {
            TeamNotificationHelper.buildNotification(contactId, fromAccount, attachment)
        } else {
            "通知消息"
        }
    }
}