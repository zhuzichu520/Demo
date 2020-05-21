package com.netease.nim.demo.ui.file.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase
import java.io.File

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/20 5:19 PM
 * since: v 1.0.0
 */
class ItemViewModelFiles(
    viewModel: ViewModelFile,
    val file: File
) : ItemViewModelBase(viewModel) {

    val fileName = MutableLiveData<String>(file.name)

    val fileIcon = MutableLiveData<Int>().apply {
        value = R.mipmap.file_ic_detail_files
    }

    val onClickCommand = createCommand {
        viewModel.loadFileList(file)
    }

}