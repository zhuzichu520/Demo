package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 文本消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ItemViewModelTextMessage(message: IMMessage) : ItemViewModelBaseMessage(message) {

    val text = MutableLiveData<String>()

    init {
        text.value = message.content
    }
}