package com.netease.nim.demo.ui.file.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.file.arg.ArgBrowseFile
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelBrowseFile @Inject constructor(
) : ViewModelBase<ArgBrowseFile>() {

    val title = MutableLiveData<String>()

}