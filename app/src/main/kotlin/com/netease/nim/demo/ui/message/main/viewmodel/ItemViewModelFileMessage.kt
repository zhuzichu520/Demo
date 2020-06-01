package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.libs.tool.byteCountToDisplaySizeTwo
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.R
import com.netease.nim.demo.ui.file.type.FileType
import com.netease.nim.demo.ui.file.type.ToolFileType
import com.netease.nim.demo.ui.message.filedownload.ActivityFileDownload
import com.netease.nim.demo.ui.message.filedownload.arg.ArgFileDownload
import com.netease.nim.demo.ui.message.main.domain.UseCaseDowloadAttachment
import com.netease.nimlib.sdk.msg.attachment.FileAttachment
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.uber.autodispose.autoDispose

/**
 * @see R.layout.item_message_file
 * desc 文本消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ItemViewModelFileMessage(
    viewModel: BaseViewModel<*>,
    message: IMMessage,
    useCaseDowloadAttachment: UseCaseDowloadAttachment
) : ItemViewModelBaseMessage(viewModel, message) {

    private var fileType: FileType? = null
    private val fileAttachment = message.attachment as FileAttachment

    init {
        fileType = ToolFileType.getFileTypeByFileName(fileAttachment.displayName)
    }

    val fileName = MutableLiveData<String>(fileAttachment.displayName)

    val size = MutableLiveData<String>().apply {
        value = byteCountToDisplaySizeTwo(fileAttachment.size)
    }

    val fileIcon = MutableLiveData<Int>().apply {
        fileType?.let {
            value = it.fileIconResId
        } ?: let {
            value = R.mipmap.file_ic_detail_unknow
        }
    }


    val onClickAttachFailedCommand = createCommand {
        useCaseDowloadAttachment.execute(UseCaseDowloadAttachment.Parameters(message, false))
            .autoDispose(viewModel).subscribe { }
    }

    val onClickFileCommand = createCommand {
        startActivity(ActivityFileDownload::class.java, ArgFileDownload(message))
    }

}