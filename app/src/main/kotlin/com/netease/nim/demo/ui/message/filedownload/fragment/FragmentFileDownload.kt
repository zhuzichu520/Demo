package com.netease.nim.demo.ui.message.filedownload.fragment

import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.hiwitech.android.libs.tool.copyFile
import com.hiwitech.android.libs.tool.deleteQuietly
import com.hiwitech.android.shared.global.CacheGlobal
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentFileDownloadBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.file.type.ToolFileType
import com.netease.nim.demo.ui.message.filedownload.arg.ArgFileDownload
import com.netease.nim.demo.ui.message.filedownload.viewmodel.ViewModelFileDownload
import com.netease.nim.demo.ui.message.filedownload.viewmodel.ViewModelFileDownload.Companion.STATE_ATTACH_FAILED
import com.netease.nim.demo.ui.message.filedownload.viewmodel.ViewModelFileDownload.Companion.STATE_ATTACH_LOADING
import com.netease.nim.demo.ui.message.filedownload.viewmodel.ViewModelFileDownload.Companion.STATE_ATTACH_NORMAL
import com.netease.nim.demo.ui.message.main.domain.UseCaseDowloadAttachment
import com.netease.nimlib.sdk.msg.attachment.FileAttachment
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.tencent.smtt.sdk.TbsReaderView
import com.uber.autodispose.autoDispose
import kotlinx.android.synthetic.main.fragment_file_download.*
import java.io.File
import java.util.*

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/21 9:01 PM
 * since: v 1.0.0
 */
class FragmentFileDownload :
    FragmentBase<FragmentFileDownloadBinding, ViewModelFileDownload, ArgFileDownload>(),
    TbsReaderView.ReaderCallback {


    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_file_download


    private lateinit var message: IMMessage

    private lateinit var view_tbs: TbsReaderView

    override fun initView() {
        super.initView()
        message = arg.message
        view_content.removeAllViews()
        view_tbs = TbsReaderView(requireContext(), this)
        view_content.addView(view_tbs)

        view_progress.setQMUIProgressBarTextGenerator { _, value, maxValue ->
            val percent = value.toFloat() / maxValue.toFloat()
            String.format(Locale.US, "%d%%", (percent * 100).toInt())
        }
        if ((message.attachment as FileAttachment).path == null) {
            //文件为空，去下载视频附件
            message.attachStatus = AttachStatusEnum.transferring
            updateView()
            viewModel.useCaseDowloadAttachment.execute(
                UseCaseDowloadAttachment.Parameters(arg.message, false)
            ).autoDispose(viewModel).subscribe { }
        } else {
            message.attachStatus = AttachStatusEnum.transferred
            updateView()
        }
    }

    private fun updateView() {
        val attachment = message.attachment as FileAttachment
        viewModel.title.value = attachment.displayName
        viewModel.attachStatus.value = when (message.attachStatus) {
            AttachStatusEnum.transferring -> {
                STATE_ATTACH_LOADING
            }
            AttachStatusEnum.fail -> {
                if (message.status != MsgStatusEnum.fail) {
                    STATE_ATTACH_FAILED
                } else {
                    STATE_ATTACH_NORMAL
                }
            }
            AttachStatusEnum.transferred -> {
                showPreViewFile(attachment)
                STATE_ATTACH_NORMAL
            }
            else -> {
                STATE_ATTACH_NORMAL
            }
        }
    }

    private fun showPreViewFile(attachment: FileAttachment) {
        //将附件移动到Download文件夹中，并且重命名
        val file = File(CacheGlobal.getDownloadDir(), attachment.displayName)
        if (file.exists() && file.length() == attachment.size) {
            deleteQuietly(file)
        }
        copyFile(attachment.path, file.absolutePath)
        val result = view_tbs.preOpen(ToolFileType.getSuffix(attachment.displayName), false)
        if (result) {
            view_tbs.openFile(
                bundleOf(
                    TbsReaderView.KEY_FILE_PATH to file.absolutePath,
                    TbsReaderView.KEY_TEMP_PATH to CacheGlobal.getTbsReaderTempCacheDir()
                )
            )
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        /**
         * 消息状态监听
         */
        viewModel.toObservable(NimEvent.OnMessageStatusEvent::class.java, Observer {
            message = it.message
            if (message.uuid == arg.message.uuid) {
                updateView()
            }
        })


        /**
         * 附件下载进度
         */
        viewModel.toObservable(NimEvent.OnAttachmentProgressEvent::class.java, Observer {
            if (arg.message.uuid != it.attachment.uuid)
                return@Observer
            val attachment = it.attachment
            val percent = attachment.transferred.toFloat() / attachment.total.toFloat()
            viewModel.progress.value = (100 * percent).toInt()
        })


        /**
         * 进度监听
         */
        viewModel.progress.observe(viewLifecycleOwner, Observer {
            view_progress.progress = it
        })
    }

    override fun onDestroyView() {
        view_tbs.onStop()
        super.onDestroyView()
    }

    override fun onCallBackAction(p0: Int?, p1: Any?, p2: Any?) {

    }

}