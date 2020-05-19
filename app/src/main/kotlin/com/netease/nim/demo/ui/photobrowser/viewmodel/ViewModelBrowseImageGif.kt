package com.netease.nim.demo.ui.photobrowser.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.message.main.domain.UseCaseDowloadAttachment
import com.netease.nim.demo.ui.photobrowser.arg.ArgBrowserGif
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.uber.autodispose.autoDispose
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/11 10:39 AM
 * since: v 1.0.0
 */
class ViewModelBrowseImageGif @Inject constructor(
    private val useCaseDowloadAttachment: UseCaseDowloadAttachment
) : ViewModelBase<ArgBrowserGif>() {

    companion object {

        //附加下载中
        private const val STATE_ATTACH_LOADING = 0

        //附件下载失败
        private const val STATE_ATTACH_FAILED = 1

        //附件下载完成
        private const val STATE_ATTACH_NORMAL = 2

    }

    /**
     * 附件下载状态
     */
    val attachStatus = MutableLiveData<Int>()

    val imagePath = MutableLiveData<Any>()

    val onClickAttachFailedCommand = createCommand {
        useCaseDowloadAttachment.execute(UseCaseDowloadAttachment.Parameters(arg.message, true))
            .autoDispose(this).subscribe { }
    }


    fun updateView(message: IMMessage) {
        val attachment = message.attachment as ImageAttachment
        val thumbPath = attachment.thumbPath
        val path = attachment.path
        imagePath.value = if (!path.isNullOrEmpty()) {
            path
        } else if (!thumbPath.isNullOrEmpty()) {
            thumbPath
        } else {
            if (message.attachStatus == AttachStatusEnum.transferred || message.attachStatus == AttachStatusEnum.def) {
                useCaseDowloadAttachment.execute(UseCaseDowloadAttachment.Parameters(message, true))
                    .autoDispose(this).subscribe { }
            }
            R.drawable.shape_bg_message_image
        }

        attachStatus.value = when (message.attachStatus) {
            AttachStatusEnum.transferring -> {
                STATE_ATTACH_LOADING
            }
            AttachStatusEnum.fail -> {
                if (message.status != MsgStatusEnum.fail) {
                    STATE_ATTACH_FAILED
                } else {
                    STATE_ATTACH_NORMAL
                }
            }
            else -> {
                STATE_ATTACH_NORMAL
            }
        }
    }
}