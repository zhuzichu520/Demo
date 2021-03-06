package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.netease.nimlib.sdk.msg.attachment.LocationAttachment
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/8 2:15 PM
 * since: v 1.0.0
 */
class ItemViewModelLocationMessage(
    viewModel: BaseViewModel<*>,
    message: IMMessage
) : ItemViewModelBaseMessage(viewModel,message) {

    val location = MutableLiveData<String>().apply {
        value = (message.attachment as LocationAttachment).address
    }

}