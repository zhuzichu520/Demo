package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.libs.tool.jumpEmail
import com.hiwitech.android.libs.tool.jumpToPhonePanel
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.createTypeCommand
import com.hiwitech.android.shared.global.AppGlobal.context
import com.hiwitech.android.shared.tools.ToolDate
import com.netease.nim.demo.R
import com.netease.nim.demo.ui.web.ActivityWeb
import com.netease.nim.demo.ui.web.arg.ArgWeb
import com.netease.nimlib.sdk.avchat.constant.AVChatRecordState
import com.netease.nimlib.sdk.avchat.constant.AVChatType
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 文本消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ItemViewModelAvchatMessage(
    viewModel: BaseViewModel<*>,
    message: IMMessage
) : ItemViewModelBaseMessage(viewModel, message) {

    val attachment = message.attachment as AVChatAttachment

    val icon = MutableLiveData<Int>().apply {
        val iconId: Int = when (attachment.type) {
            AVChatType.VIDEO -> {
                R.drawable.ic_nim_avchat_video
            }
            AVChatType.AUDIO -> {
                R.drawable.ic_nim_avchat_audio
            }
            else -> {
                R.drawable.ic_nim_avchat_audio
            }
        }
        value = iconId
    }


    val text = MutableLiveData<String>().apply {
        val content: String = when (attachment.state) {
            AVChatRecordState.Success -> {
                "聊天时长 ${ToolDate.secToTime(attachment.duration)}"
            }
            AVChatRecordState.Missed -> {
                "对方已取消"
            }
            AVChatRecordState.Rejected -> {
                if (isMine()) "对方已拒绝" else "已拒绝"
            }
            AVChatRecordState.Canceled -> {
                "已取消"
            }
            else -> {
                "未知通话状态"
            }
        }
        value = content
    }

}