package com.netease.nim.demo.ui.file.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.libs.tool.byteCountToDisplaySizeTwo
import com.hiwitech.android.libs.tool.contentEquals
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.toast
import com.hiwitech.android.shared.tools.ToolDate
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.ui.file.ActivityBrowseFile
import com.netease.nim.demo.ui.file.arg.ArgBrowseFile
import com.netease.nim.demo.ui.file.type.FileType
import com.netease.nim.demo.ui.file.type.ToolFileType
import java.io.File

/**
 * @see R.layout.item_file
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

    val checked = MutableLiveData<Boolean>().apply {
        value = viewModel.listSelected.value?.contains(this@ItemViewModelFile) ?: false
    }

    val fileName = MutableLiveData<String>(file.name)

    val size = MutableLiveData<String>().apply {
        value = byteCountToDisplaySizeTwo(file.length())
    }

    val date = MutableLiveData<String>().apply {
        value = ToolDate.getTimeShowString(file.lastModified(), false)
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
        if (!ToolFileType.isPreViewFile(fileType)) {
            "不支持查看类型".toast()
            return@createCommand
        }
        viewModel.startActivity(
            ActivityBrowseFile::class.java,
            arg = ArgBrowseFile(file.absolutePath, file.name, suffix.toString())
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemViewModelFile

        return contentEquals(other.file, this.file)
    }

    override fun hashCode(): Int {
        return file.hashCode()
    }


}