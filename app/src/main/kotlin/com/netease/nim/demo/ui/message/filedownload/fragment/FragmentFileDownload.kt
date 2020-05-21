package com.netease.nim.demo.ui.message.filedownload.fragment

import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentFileDownloadBinding
import com.netease.nim.demo.ui.message.filedownload.arg.ArgFileDownload
import com.netease.nim.demo.ui.message.filedownload.viewmodel.ViewModelFileDownload
import com.netease.nimlib.sdk.msg.attachment.FileAttachment
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/21 9:01 PM
 * since: v 1.0.0
 */
class FragmentFileDownload :
    FragmentBase<FragmentFileDownloadBinding, ViewModelFileDownload, ArgFileDownload>() {

    private lateinit var message: IMMessage

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_file_download

    override fun initView() {
        super.initView()
        message = arg.message
    }

    override fun initOneData() {
        super.initOneData()
        updateView()
    }

    private fun updateView() {
        val attachment = message.attachment as FileAttachment
        viewModel.title.value = attachment.displayName
    }

}