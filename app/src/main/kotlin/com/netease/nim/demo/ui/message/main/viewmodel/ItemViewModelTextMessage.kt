package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nimlib.sdk.msg.model.IMMessage

class ItemViewModelTextMessage(message: IMMessage) : ItemViewModelBaseMessage(message) {

    val text = MutableLiveData<String>()

    init {
        text.value = message.content
    }
}