package com.netease.nim.demo.ui.photobrowser.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.message.main.domain.UseCaseDowloadAttachment
import com.netease.nim.demo.ui.photobrowser.arg.ArgBrowser
import com.uber.autodispose.autoDispose
import javax.inject.Inject


/**
 * desc
 * author: 朱子楚
 * time: 2020/5/11 10:39 AM
 * since: v 1.0.0
 */
class ViewModelBrowseVideo @Inject constructor(
    val useCaseDowloadAttachment: UseCaseDowloadAttachment
) : ViewModelBase<ArgBrowser>() {

    val progress = MutableLiveData<Int>()

    /**
     * 附件下载状态
     */
    val attachStatus = MutableLiveData<Int>()

    val imagePath = MutableLiveData<Any>()

    val onClickAttachFailedCommand = createCommand {
        useCaseDowloadAttachment.execute(UseCaseDowloadAttachment.Parameters(arg.message, false))
            .autoDispose(this).subscribe { }
    }


}