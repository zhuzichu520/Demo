package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nimlib.sdk.msg.model.IMMessage

class ItemViewModelPictureMessage(message: IMMessage) : ItemViewModelBaseMessage(message) {

    private val picture = MutableLiveData<String>()

}