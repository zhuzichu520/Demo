package com.netease.nim.demo.ui.message.main.viewmodel

import com.hiwitech.android.mvvm.base.BaseViewModel
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 未知消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ItemViewModelUnknownMessage(
    viewModel: BaseViewModel<*>,
    message: IMMessage
) : ItemViewModelBaseMessage(viewModel,message) {

}