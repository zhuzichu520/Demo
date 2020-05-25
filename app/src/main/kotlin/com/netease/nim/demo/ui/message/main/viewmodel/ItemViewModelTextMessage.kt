package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.createTypeCommand
import com.netease.nim.demo.R
import com.netease.nim.demo.ui.web.arg.ArgWeb
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 文本消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ItemViewModelTextMessage(
    viewModel: BaseViewModel<*>,
    message: IMMessage
) : ItemViewModelBaseMessage(viewModel, message) {

    val text = MutableLiveData<String>()

    init {
        text.value = message.content
    }

    val onClickLinkUrlCommand = createTypeCommand<String> {
        start(R.id.action_global_fragmentMessage_activityWeb, ArgWeb(this))
    }

}