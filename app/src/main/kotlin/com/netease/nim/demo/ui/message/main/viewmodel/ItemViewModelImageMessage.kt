package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.nim.tools.ToolImage
import com.netease.nim.demo.ui.message.main.domain.UseCaseDowloadAttachment
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 图片消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
data class ItemViewModelImageMessage(
    var msg: IMMessage,
    val useCaseDowloadAttachment: UseCaseDowloadAttachment,
    val progress: String? = null
) : ItemViewModelBaseMessage(msg) {

    val attachment = message.attachment as ImageAttachment

    private val imageSize = ToolImage.getThumbnailDisplaySize(
        attachment.width.toFloat(),
        attachment.height.toFloat(),
        ToolImage.getImageMaxEdge().toFloat(),
        ToolImage.getImageMinEdge().toFloat()
    )

    /**
     * 图片高
     */
    val imageHeight = MutableLiveData<Int>().apply {
        value = imageSize.height
    }

    /**
     * 图片宽
     */
    val imageWidth = MutableLiveData<Int>().apply {
        value = imageSize.width
    }

    /**
     * 缩略图地址（下载到本地缩略图片）
     */
    val image = MutableLiveData<String>().apply {
        val thumbPath = attachment.thumbPath
        val path = attachment.path
        when {
            !thumbPath.isNullOrEmpty() -> {
                value = thumbPath
            }
            !path.isNullOrEmpty() -> {
                value = path
            }
            else -> {
                if (message.attachStatus == AttachStatusEnum.def || message.attachStatus == AttachStatusEnum.transferred)
                    useCaseDowloadAttachment.execute(
                        UseCaseDowloadAttachment.Parameters(message, true)
                    ).subscribe {
                    }
            }
        }
    }

    /**
     * 附件下载进度
     */
    val attachmentProgress = MutableLiveData<String>(progress)

}