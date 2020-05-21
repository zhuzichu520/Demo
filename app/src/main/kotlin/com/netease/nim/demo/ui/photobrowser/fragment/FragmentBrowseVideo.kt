package com.netease.nim.demo.ui.photobrowser.fragment

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import com.hiwitech.android.shared.glide.GlideApp
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentBrowseVideoBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.message.main.domain.UseCaseDowloadAttachment
import com.netease.nim.demo.ui.photobrowser.arg.ArgBrowser
import com.netease.nim.demo.ui.photobrowser.viewmodel.ViewModelBrowseVideo
import com.netease.nimlib.sdk.msg.attachment.VideoAttachment
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.uber.autodispose.autoDispose
import kotlinx.android.synthetic.main.fragment_browse_video.*
import java.util.*

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/12 2:51 PM
 * since: v 1.0.0
 */
class FragmentBrowseVideo :
    FragmentBase<FragmentBrowseVideoBinding, ViewModelBrowseVideo, ArgBrowser>() {

    companion object {

        //附加下载中
        private const val STATE_ATTACH_LOADING = 0

        //附件下载失败
        private const val STATE_ATTACH_FAILED = 1

        //附件下载完成
        private const val STATE_ATTACH_NORMAL = 2

    }

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_browse_video

    lateinit var view_image: AppCompatImageView

    private lateinit var message: IMMessage


    override fun initView() {
        super.initView()
        message = arg.message
        view_image = AppCompatImageView(requireContext())

        view_progress.setQMUIProgressBarTextGenerator { _, value, maxValue ->
            val percent = value.toFloat() / maxValue.toFloat()
            String.format(Locale.US, "%d%%", (percent * 100).toInt())
        }
        if ((message.attachment as VideoAttachment).path == null) {
            //文件为空，去下载视频附件
            message.attachStatus = AttachStatusEnum.transferring
            updateView()
            view_player.showControlView(false)
            viewModel.useCaseDowloadAttachment.execute(
                UseCaseDowloadAttachment.Parameters(arg.message, false)
            ).autoDispose(viewModel).subscribe { }
        } else {
            message.attachStatus = AttachStatusEnum.transferred
            updateView()
        }
    }

    override fun initListener() {
        super.initListener()
        view_player.setOnClickCloseListener(View.OnClickListener { viewModel.back() })
    }

    private fun initializePlayer() {
        val gsyVideoOption = GSYVideoOptionBuilder()
        gsyVideoOption
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setAutoFullWithSize(true)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setThumbImageView(view_image)
            .setUrl((message.attachment as VideoAttachment).path)
            .setCacheWithPlay(true)
            .setLooping(false)
            .build(view_player)
        view_player.showControlView(true)
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

        viewModel.imagePath.observe(viewLifecycleOwner, Observer {
            GlideApp.with(view_image).load(it).into(view_image)
        })
    }


    private fun updateView() {
        val attachment = message.attachment as VideoAttachment
        val path = attachment.path
        val thumbPath = attachment.thumbPath
        viewModel.imagePath.value = if (!path.isNullOrEmpty()) {
            path
        } else if (!thumbPath.isNullOrEmpty()) {
            thumbPath
        } else {
            if (message.attachStatus == AttachStatusEnum.transferred || message.attachStatus == AttachStatusEnum.def) {
                viewModel.useCaseDowloadAttachment.execute(
                    UseCaseDowloadAttachment.Parameters(message, true)
                ).autoDispose(viewModel).subscribe { }
            }
            R.drawable.transparent
        }

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
                initializePlayer()
                STATE_ATTACH_NORMAL
            }
            else -> {
                STATE_ATTACH_NORMAL
            }
        }
    }

    override fun onResume() {
        super.onResume()
        view_player.onVideoResume()
    }

    override fun onPause() {
        super.onPause()
        view_player.onVideoPause()
    }

}