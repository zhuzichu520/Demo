package com.netease.nim.demo.ui.message.main.viewmodel

import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.ui.message.main.domain.UseCaseDowloadAttachment
import com.netease.nim.demo.ui.photobrowser.ActivityPhotoBrowserGif
import com.netease.nim.demo.ui.photobrowser.arg.ArgBrowserGif
import com.netease.nim.demo.ui.photobrowser.domain.UseCaseGetImageAndVideoMessage
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 图片消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ItemViewModelImageGifMessage(
    viewModel: BaseViewModel<*>,
    message: IMMessage,
    useCaseDowloadAttachment: UseCaseDowloadAttachment,
    useCaseGetImageAndVideoMessage: UseCaseGetImageAndVideoMessage
) : ItemViewModelImageMessage(
    viewModel,
    message,
    useCaseDowloadAttachment,
    useCaseGetImageAndVideoMessage
) {

    val onClickImageGifCommand = createCommand {
        startActivity(ActivityPhotoBrowserGif::class.java, arg = ArgBrowserGif(message))
    }

}