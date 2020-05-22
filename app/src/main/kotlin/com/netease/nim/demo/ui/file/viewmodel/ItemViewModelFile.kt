package com.netease.nim.demo.ui.file.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.libs.tool.byteCountToDisplaySize
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.tools.ToolDate
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.ui.file.arg.ArgBrowseFile
import com.netease.nim.demo.ui.file.type.FileType
import com.netease.nim.demo.ui.file.type.ToolFileType
import java.io.File

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/20 5:19 PM
 * since: v 1.0.0
 */
class ItemViewModelFile(
    viewModel: ViewModelFile,
    val file: File
) : ItemViewModelBase(viewModel) {

    private var fileType: FileType? = null

    private var suffix: String? = null

    init {
        fileType = ToolFileType.getFileTypeByFileName(file.name)
        suffix = ToolFileType.getSuffix(file.name)
    }

    val fileName = MutableLiveData<String>(file.name)

    val size = MutableLiveData<String>().apply {
        value = byteCountToDisplaySize(file.length())
    }

    val date = MutableLiveData<String>().apply {
        value = ToolDate.getTimeShowString(file.lastModified(), false)
    }

    val see = MutableLiveData<Boolean>().apply {
        value = ToolFileType.isPreViewFile(fileType)
    }

    val fileIcon = MutableLiveData<Int>().apply {
        fileType?.let {
            value = it.fileIconResId
        } ?: let {
            value = R.mipmap.file_ic_detail_unknow
        }
    }

    val onClickCommand = createCommand {
        viewModel.onClickItemEvent.value = this
    }

    val onClickSeeCommand = createCommand {
        viewModel.start(
            R.id.action_fragmentFile_to_activityBrowseFile,
            arg = ArgBrowseFile(file.absolutePath, file.name, suffix.toString())
        )
    }

}