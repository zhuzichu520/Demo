package com.netease.nim.demo.ui.message.main.viewmodel

import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.createTypeCommand
import com.netease.nim.demo.R
import com.netease.nim.demo.nim.tools.ToolImage
import com.netease.nim.demo.ui.message.main.arg.ArgPhotoBrowser
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 图片消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ItemViewModelImageMessage(
    viewModel: BaseViewModel<*>,
    message: IMMessage
) : ItemViewModelBaseMessage(viewModel, message) {

    var attachment = (message.attachment as ImageAttachment)

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
    val imageUrl = MutableLiveData<Any>().apply {
        val thumbPath = attachment.thumbPath
        val path = attachment.path
        value = if (!path.isNullOrEmpty()) {
            path
        } else if (!thumbPath.isNullOrEmpty()) {
            thumbPath
        } else {
            R.drawable.shape_bg_message_image
        }
    }

    val onClickImageCommand = createTypeCommand<ImageView> {
        start(R.id.action_fragmentMessage_to_activityPhotoBrowser, arg = ArgPhotoBrowser(listOf()))
    }
}