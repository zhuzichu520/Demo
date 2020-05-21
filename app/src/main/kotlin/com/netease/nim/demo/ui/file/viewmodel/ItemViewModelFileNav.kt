package com.netease.nim.demo.ui.file.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.base.ItemViewModelBase
import java.io.File

/**
 * Created by Android Studio.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2019-08-14
 * Time: 11:48
 */
class ItemViewModelFileNav(
    viewModel: ViewModelFile,
    val file: File
) : ItemViewModelBase(viewModel) {

    val fileName = MutableLiveData<String>(file.name)

    val onClickCommand = createCommand {
        viewModel.navList.update(viewModel.navList.dropLastWhile { file.path != (it as ItemViewModelFileNav).file.path })
        viewModel.loadFileList(file, false)
    }

}