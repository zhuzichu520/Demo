package com.netease.nim.demo.ui.message.filedownload.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.message.filedownload.arg.ArgFileDownload
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/13 2:45 PM
 * since: v 1.0.0
 */
class ViewModelFileDownload @Inject constructor(
) : ViewModelBase<ArgFileDownload>() {

    val title = MutableLiveData<String>()

}