package com.netease.nim.demo.ui.avchat.fragment

import androidx.activity.addCallback
import androidx.lifecycle.Observer
import com.hiwitech.android.shared.ext.logi
import com.hiwitech.android.shared.ext.toStringByResId
import com.hiwitech.android.shared.ext.toast
import com.netease.nim.demo.R
import com.netease.nim.demo.BR
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentAvchatAudioBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.avchat.arg.ArgAvchat
import com.netease.nim.demo.ui.avchat.viewmodel.ViewModelAvchatAudio
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/15 8:26 PM
 * since: v 1.0.0
 */
class FragmentAvchatAudio :
    FragmentBase<FragmentAvchatAudioBinding, ViewModelAvchatAudio, ArgAvchat>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_avchat_audio

    override fun initView() {
        super.initView()
        initBackListener()
        viewModel.loadUser()

        when (arg.type) {
            ArgAvchat.TYPE_OUTGOING -> {
                handleOutgoing()
            }
            ArgAvchat.TYPE_INCOMING -> {
                handleIncoming()
            }
        }
    }

    private fun handleIncoming() {
        viewModel.title.value = R.string.avchat_audio_call_request.toStringByResId(requireContext())
        viewModel.showIncoming()
    }

    private fun handleOutgoing() {
        viewModel.title.value = R.string.avchat_wait_recieve.toStringByResId(requireContext())
        viewModel.showOutgoging()
        viewModel.doCalling()
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.toObservable(NimEvent.OnHangUpNotificationEvent::class.java, Observer {
            finishAndRemoveTask()
        })

        viewModel.toObservable(NimEvent.OnCalleeAckNotificationEvent::class.java, Observer {
            when (it.event.event) {
                /**
                 * 被叫方同意通话
                 */
                AVChatEventType.CALLEE_ACK_AGREE -> {
                    "被叫方同意通话".logi()
                }
                /**
                 * 被叫方拒绝通话
                 */
                AVChatEventType.CALLEE_ACK_REJECT -> {
                    "被叫方拒绝通话".logi()
                    "对方拒绝通话".toast()
                    finishAndRemoveTask()
                }
                /**
                 * 被叫方正在忙
                 */
                AVChatEventType.CALLEE_ACK_BUSY -> {
                    "被叫方正在忙".logi()
                    "对方正在忙".toast()
                    finishAndRemoveTask()
                }
                /**
                 * 被叫方同时在线的其他端同意通话
                 */
                AVChatEventType.CALLEE_ONLINE_CLIENT_ACK_AGREE -> {
                    "被叫方同时在线的其他端同意通话".logi()
                }
                /**
                 * 被叫方同时在线的其他端拒绝通话
                 */
                AVChatEventType.CALLEE_ONLINE_CLIENT_ACK_REJECT -> {
                    "被叫方同时在线的其他端拒绝通话".logi()
                }
                /**
                 * 对方挂断电话
                 */
                AVChatEventType.PEER_HANG_UP -> {
                    "对方挂断电话".logi()
                }
                /**
                 * 通话中收到的控制命令
                 */
                AVChatEventType.CONTROL_NOTIFICATION -> {
                    "通话中收到的控制命令".logi()
                }
                else -> {
                }
            }
        })
    }

   private fun finishAndRemoveTask() {
        requireActivity().finishAndRemoveTask()
    }

    private fun initBackListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }


}