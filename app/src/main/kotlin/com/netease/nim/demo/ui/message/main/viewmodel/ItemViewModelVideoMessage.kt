package com.netease.nim.demo.ui.message.main.viewmodel

import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.lifecycle.MutableLiveData
import androidx.navigation.ActivityNavigator
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.createTypeCommand
import com.hiwitech.android.shared.tools.ToolDate
import com.hiwitech.android.shared.tools.Weak
import com.netease.nim.demo.R
import com.netease.nim.demo.nim.tools.ToolImage
import com.netease.nim.demo.ui.message.main.domain.UseCaseDowloadAttachment
import com.netease.nim.demo.ui.photobrowser.ActivityPhotoBrowser
import com.netease.nim.demo.ui.photobrowser.arg.ArgPhotoBrowser
import com.netease.nim.demo.ui.photobrowser.domain.UseCaseGetImageAndVideoMessage
import com.netease.nim.demo.ui.photobrowser.fragment.FragmentPhotoBrowser.Companion.TRANSITION_NAME
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.uber.autodispose.autoDispose

/**
 * desc 图片消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
open class ItemViewModelVideoMessage(
    viewModel: BaseViewModel<*>,
    message: IMMessage,
    useCaseDowloadAttachment: UseCaseDowloadAttachment,
    useCaseGetImageAndVideoMessage: UseCaseGetImageAndVideoMessage
) : ItemViewModelBaseMessage(viewModel, message) {

    var photoView by Weak<ImageView>()

    var attachment = (message.attachment as VideoAttachment)

    private val imageSize = ToolImage.getThumbnailDisplaySize(
        attachment.width.toFloat(),
        attachment.height.toFloat(),
        ToolImage.getImageMaxEdge().toFloat(),
        ToolImage.getImageMinEdge().toFloat()
    )

    val duration = MutableLiveData<String>().apply {
        value = ToolDate.secToTime((attachment.duration / 1000).toInt())
    }


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
            if (message.attachStatus == AttachStatusEnum.transferred || message.attachStatus == AttachStatusEnum.def) {
                useCaseDowloadAttachment.execute(UseCaseDowloadAttachment.Parameters(message, true))
                    .autoDispose(viewModel).subscribe { }
            }
            R.drawable.shape_bg_message_image
        }
    }

    val onClickAttachFailedCommand = createCommand {
        useCaseDowloadAttachment.execute(UseCaseDowloadAttachment.Parameters(message, true))
            .autoDispose(viewModel).subscribe { }
    }

    val onClickImageCommand = createTypeCommand<ImageView> {
        useCaseGetImageAndVideoMessage.execute(message).autoDispose(viewModel)
            .subscribe {
                (viewModel as? ViewModelMessage)?.shareElementUuid?.value = uuid
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this.context as Activity, this, TRANSITION_NAME
                    )
                startActivity(
                    ActivityPhotoBrowser::class.java,
                    arg = ArgPhotoBrowser(message, it),
                    options = optionsCompat.toBundle()
                )
            }
    }

    val initImageView = createTypeCommand<ImageView> {
        photoView = this
        if (uuid == (viewModel as? ViewModelMessage)?.shareElementUuid?.value) {
            ActivityCompat.setExitSharedElementCallback(this.context as Activity, object :
                SharedElementCallback() {
                override fun onMapSharedElements(
                    names: MutableList<String>,
                    sharedElements: MutableMap<String, View>
                ) {
                    sharedElements.clear()
                    sharedElements[TRANSITION_NAME] = this@createTypeCommand
                }
            })
        }
    }

}