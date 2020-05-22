package com.netease.nim.demo.ui.message.filedownload.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.message.filedownload.arg.ArgFileDownload
import com.netease.nim.demo.ui.message.main.domain.UseCaseDowloadAttachment
import com.uber.autodispose.autoDispose
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/13 2:45 PM
 * since: v 1.0.0
 */
class ViewModelFileDownload @Inject constructor(
    val useCaseDowloadAttachment: UseCaseDowloadAttachment
) : ViewModelBase<ArgFileDownload>() {


    companion object {

        //附加下载中
        const val STATE_ATTACH_LOADING = 0

        //附件下载失败
        const val STATE_ATTACH_FAILED = 1

        //附件下载完成
        const val STATE_ATTACH_NORMAL = 2

    }

    val progress = MutableLiveData<Int>()

    /**
     * 附件下载状态
     */
    val attachStatus = MutableLiveData<Int>()

    val title = MutableLiveData<String>()

    val onClickAttachFailedCommand = createCommand {
        useCaseDowloadAttachment.execute(UseCaseDowloadAttachment.Parameters(arg.message, false))
            .autoDispose(this).subscribe { }
    }

}