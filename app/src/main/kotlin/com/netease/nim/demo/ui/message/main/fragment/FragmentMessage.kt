package com.netease.nim.demo.ui.message.main.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.shared.bus.LiveDataBus
import com.hiwitech.android.shared.ext.closeDefaultAnimator
import com.hiwitech.android.shared.ext.toEditable
import com.hiwitech.android.shared.ext.toast
import com.hiwitech.android.shared.global.CacheGlobal
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.SharedViewModel
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentMessageBinding
import com.netease.nim.demo.nim.attachment.StickerAttachment
import com.netease.nim.demo.nim.emoji.StickerItem
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.launcher.event.OnKeyboardChangeEvent
import com.netease.nim.demo.ui.message.emoticon.event.EventEmoticon
import com.netease.nim.demo.ui.message.emoticon.fragment.FragmentEmoticon
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.netease.nim.demo.ui.message.main.event.EventMessage
import com.netease.nim.demo.ui.message.main.viewmodel.ViewModelMessage
import com.netease.nim.demo.ui.message.more.event.EventMore
import com.netease.nim.demo.ui.message.more.fragment.FragmentMore
import com.netease.nim.demo.ui.message.more.viewmodel.ViewModelMore
import com.netease.nim.demo.ui.message.view.ViewMessageInput
import com.netease.nim.demo.ui.message.view.ViewMessageInput.Companion.TYPE_EMOJI
import com.netease.nim.demo.ui.message.view.ViewMessageInput.Companion.TYPE_MORE
import com.netease.nimlib.sdk.media.record.AudioRecorder
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback
import com.netease.nimlib.sdk.media.record.RecordType
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uber.autodispose.autoDispose
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.layout_input.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*
import javax.inject.Inject


/**
 * desc 消息界面Frqagment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentMessage : FragmentBase<FragmentMessageBinding, ViewModelMessage, ArgMessage>() {

    companion object {
        const val REQUEST_CODE_CHOOSE = 0x11
    }

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_message

    private lateinit var sessionType: SessionTypeEnum

    private lateinit var recordAnima: AnimationDrawable

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private lateinit var audioRecorder: AudioRecorder

    @Inject
    lateinit var msgService: MsgService

    override fun initArgs(arg: ArgMessage) {
        super.initArgs(arg)
        sessionType = SessionTypeEnum.typeOfValue(arg.sessionType)
    }

    override fun initLazyData() {
        super.initLazyData()
        loadData(sessionType)
        viewModel.loadMessage(
            MessageBuilder.createEmptyMessage(arg.contactId, sessionType, 0),
            isFirst = true
        )
    }

    override fun initView() {
        super.initView()
        audioRecorder = AudioRecorder(context, RecordType.AAC, 60, object : IAudioRecordCallback {
            override fun onRecordSuccess(
                audioFile: File,
                audioLength: Long,
                recordType: RecordType
            ) {
                val audioMessage =
                    MessageBuilder.createAudioMessage(
                        arg.contactId,
                        sessionType,
                        audioFile,
                        audioLength
                    )
                viewModel.sendMessage(audioMessage)
            }

            override fun onRecordReachedMaxTime(maxTime: Int) {

            }

            override fun onRecordReady() {

            }

            override fun onRecordCancel() {

            }

            override fun onRecordStart(audioFile: File, recordType: RecordType) {

            }

            override fun onRecordFail() {
                R.string.recording_error.toast()
            }
        })
        recycler.closeDefaultAnimator()
        recordAnima = view_record.background as AnimationDrawable
        initBottomFragment()
        initBackListener()
    }

    /**
     * todo 内存泄漏
     * 初始化底部布局的Fragment，有Emoji表情，有更多布局
     */
    private fun initBottomFragment() {
        val fragmentEmoji = FragmentEmoticon()
        val fragmentMore = FragmentMore()
        recycler.post {
            message_input.attachContentView(layout_content, recycler)
            message_input.setInputType(ViewMessageInput.TYPE_DEFAULT)
        }
        message_input.audioRecorder = audioRecorder
        // 切换Fragment
        message_input.onReplaceFragment = {
            when (this) {
                TYPE_EMOJI -> {
                    fragmentEmoji
                }
                TYPE_MORE -> {
                    fragmentMore
                }
                else -> {
                    fragmentEmoji
                }
            }
        }
        // 点击发送
        message_input.onClickSendListener = {
            sendTextMessage(this)
        }
    }

    private fun appendText(text: String?) {
        center_input.text = center_input.text.toString().plus(text).toEditable()
    }

    /**
     * 返回监听
     */
    private fun initBackListener() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (TYPE_EMOJI == message_input.getInputType() || TYPE_MORE == message_input.getInputType()) {
                message_input.setInputType(ViewMessageInput.TYPE_DEFAULT)
            } else {
                navController.popBackStack()
            }
        }
    }

    /**
     * 加载相应的会话数据
     * @param sessionType 会话类型
     */
    private fun loadData(sessionType: SessionTypeEnum) {
        when (sessionType) {
            SessionTypeEnum.P2P -> {
                viewModel.loadUserInfo()
            }
            SessionTypeEnum.Team -> {
                viewModel.loadTeamInfo()
            }
            else -> {

            }
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        /**
         * recyclerview滑动到指定位置
         */
        viewModel.onScrollBottomEvent.observe(viewLifecycleOwner, Observer {
            scrollToBottom()
        })

        /**
         * 添加数据完成
         */
        viewModel.onAddMessageCompletedEvent.observe(viewLifecycleOwner, Observer {
            if (isLastVisible()) {
                //最后一条可见 滑动到底部
                scrollToBottom()
            } else {
                //todo zhuzichu 最后一条不可见 显示提醒有新消息
            }
        })

        /**
         * 消息接收监听
         */
        LiveDataBus.toObservable(NimEvent.OnReceiveMessageEvent::class.java)
            .observe(viewLifecycleOwner, Observer {
                val data = it.list.filter { item ->
                    item.sessionId == arg.contactId
                }
                viewModel.addMessage(data)
            })
        /**
         * 消息发送状态监听
         */
        LiveDataBus.toObservable(NimEvent.OnMessageStatusEvent::class.java)
            .observe(viewLifecycleOwner, Observer {
                val message = it.message
                if (message.sessionId == arg.contactId) {
                    viewModel.addMessage(listOf(it.message))
                }
            })
        /**
         * 点击Emoji事件
         */
        LiveDataBus.toObservable(EventEmoticon.OnClickEmojiEvent::class.java)
            .observe(viewLifecycleOwner, Observer {
                appendText(it.text)
                center_input.setSelection(center_input.length())
            })

        /**
         * 点击贴图事件
         */
        LiveDataBus.toObservable(EventEmoticon.OnClickStickerEvent::class.java)
            .observe(viewLifecycleOwner, Observer {
                sendStickerMessage(it.stickerItem)
            })

        /**
         * 录音计时事件
         */
        LiveDataBus.toObservable(EventMessage.OnRecordAudioEvent::class.java)
            .observe(viewLifecycleOwner, Observer {
                var isShownRecord = false
                when (it.recordType) {
                    EventMessage.RECORD_SEND -> {
                        //发送语言
                        isShownRecord = false
                        recordAnima.stop()
                    }
                    EventMessage.RECORD_RECORDING -> {
                        //正在录制语言
                        isShownRecord = true
                        recordAnima.start()
                    }
                    EventMessage.RECORD_CANCEL -> {
                        //取消发送
                        isShownRecord = false
                        recordAnima.stop()
                    }
                }
                viewModel.isShownRecord.value = isShownRecord
                viewModel.recordTime.value = it.recordSecond.toString()
            })

        /**
         * 监听是否可以取消录制事件
         */
        LiveDataBus.toObservable(EventMessage.OnRecordCancelChangeEvent::class.java)
            .observe(viewLifecycleOwner, Observer {
                updateTimerTip(it.cancelled)
            })

        /**
         * 键盘高度改变事件
         */
        LiveDataBus.toObservable(OnKeyboardChangeEvent::class.java)
            .observe(viewLifecycleOwner, Observer {
                message_input.setInputType(ViewMessageInput.TYPE_INPUT)
            })

        /**
         *  点击更多中的item事件
         */
        LiveDataBus.toObservable(EventMore.OnClickItemMoreEvent::class.java)
            .observe(viewLifecycleOwner, Observer {
                when (it.type) {
                    ViewModelMore.TYPE_ALBUM -> {
                        startAlbum()
                    }
                    else -> {
                    }
                }
            })

        /**
         * 附件下载监听
         */
        LiveDataBus.toObservable(NimEvent.OnAttachmentProgressEvent::class.java)
            .observe(viewLifecycleOwner, Observer {
                viewModel.updateAttachmenntProgress(it.attachment)
            })
    }

    /**
     * 跳转到相册
     */
    private fun startAlbum() {
        RxPermissions(requireActivity()).request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).autoDispose(viewModel).subscribe {
            Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(9)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .theme(R.style.Widget_MyTheme_Zhihu)
                .showPreview(false) // Default is `true`
                .forResult(REQUEST_CODE_CHOOSE)
        }
    }

    /**
     * 正在进行语音录制和取消语音录制，界面展示
     *
     * @param cancel
     */
    private fun updateTimerTip(cancel: Boolean) {
        if (cancel) {
            viewModel.timerTip.value = R.string.recording_cancel_tip
            viewModel.timerTipBackgroundColor.value = R.drawable.nim_cancel_record_red_bg
        } else {
            viewModel.timerTip.value = R.string.recording_cancel
            viewModel.timerTipBackgroundColor.value = 0
        }
    }

    /**
     * 发送文本消息
     */
    private fun sendTextMessage(text: String) {
        val message = MessageBuilder.createTextMessage(arg.contactId, sessionType, text)
        viewModel.sendMessage(message)
    }

    /**
     * 发送贴图消息
     */
    private fun sendStickerMessage(stickerItem: StickerItem) {
        val attachment = StickerAttachment(stickerItem.category, stickerItem.name)
        val stickerMessage = MessageBuilder.createCustomMessage(
            arg.contactId, sessionType, "贴图消息", attachment
        )
        viewModel.sendMessage(stickerMessage)
    }

    /**
     * 发送图片消息
     */
    private fun sendImageMessage(paths: List<String>) {
        Luban.with(requireContext())
            .load(paths)
            .ignoreBy(100)
            .setTargetDir(CacheGlobal.getLubanCacheDir())
            .filter {
                !(TextUtils.isEmpty(it) || it.toLowerCase(Locale.getDefault()).endsWith(".gif"))
            }.setCompressListener(object : OnCompressListener {
                override fun onSuccess(file: File) {
                    val message =
                        MessageBuilder.createImageMessage(arg.contactId, sessionType, file)
                    viewModel.sendMessage(message, false)
                }

                override fun onError(e: Throwable) {
                }

                override fun onStart() {
                }

            }).launch()
    }

    override fun onResume() {
        super.onResume()
        msgService.setChattingAccount(arg.contactId, sessionType)
    }


    override fun onPause() {
        super.onPause()
        msgService.setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None)
    }

    /**
     * 滑动到置顶位置
     */
    private fun scrollToBottom() {
        MainHandler.postDelayed(50) {
            recycler.scrollBy(0, Int.MAX_VALUE)
        }
    }

    /**
     * 判断最后一条是否可见
     */
    private fun isLastVisible(): Boolean {
        val position = viewModel.items.size - 1
        val findLastPosition =
            (recycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() + 1
        return findLastPosition == position
    }

    override fun onDestroyView() {
        message_input.releaseTimeDisposable()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseRecordAnima()
    }

    /**
     * 释放帧动画资源
     */
    private fun releaseRecordAnima() {
        recordAnima.stop()
        for (i in 0 until recordAnima.numberOfFrames) {
            val frame: Drawable = recordAnima.getFrame(i)
            if (frame is BitmapDrawable) {
                frame.bitmap.recycle()
            }
            frame.callback = null
        }
        recordAnima.callback = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                sendImageMessage(Matisse.obtainPathResult(it))
            }
        }
    }

}