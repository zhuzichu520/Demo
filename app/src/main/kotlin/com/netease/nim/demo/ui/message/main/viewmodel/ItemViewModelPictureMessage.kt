package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 图片消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ItemViewModelPictureMessage(message: IMMessage) : ItemViewModelBaseMessage(message) {

    private val picture = MutableLiveData<String>()

}