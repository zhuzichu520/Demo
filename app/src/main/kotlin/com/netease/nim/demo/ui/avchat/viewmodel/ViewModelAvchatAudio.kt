package com.netease.nim.demo.ui.avchat.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.logi
import com.hiwitech.android.shared.ext.toStringByResId
import com.hiwitech.android.shared.ext.toast
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.avchat.arg.ArgAvchat
import com.netease.nim.demo.ui.avchat.domain.UseCaseAccept
import com.netease.nim.demo.ui.avchat.domain.UseCaseAudioCalling
import com.netease.nim.demo.ui.avchat.domain.UseCaseHangUp
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetUserInfo
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import com.uber.autodispose.autoDispose
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/29 11:33 AM
 * since: v 1.0.0
 */
class ViewModelAvchatAudio @Inject constructor(
    private val useCaseGetUserInfo: UseCaseGetUserInfo,
    private val useCaseAudioCalling: UseCaseAudioCalling,
    private val useCaseHangUp: UseCaseHangUp,
    private val useCaseAccept: UseCaseAccept
) : ViewModelBase<ArgAvchat>() {

    val avatar = MutableLiveData<String>()

    val title = MutableLiveData<Int>()

    val showIncoming = MutableLiveData<Boolean>()

    val showOutgoging = MutableLiveData<Boolean>()

    /**
     * 头像错误图片
     */
    val error = MutableLiveData(R.mipmap.nim_avatar_default)

    /**
     * 头像占位图
     */
    val placeholder = MutableLiveData(R.mipmap.nim_avatar_default)

    /**
     * 挂断电话
     */
    val onHangeUpCommand = createCommand {
        arg.data?.let { data ->
            useCaseHangUp.execute(data.chatId)
                .autoDispose(this)
                .subscribe(
                    {
                        finish()
                    },
                    {
                        handleThrowable(it)
                    }
                )
        }
    }

    /**
     * 接听电话
     */
    val onAcceptCommand = createCommand {
        arg.data?.let { data ->
            useCaseAccept.execute(data.chatId)
                .autoDispose(this)
                .subscribe(
                    {
                        showAcceptSuccess()
                    },
                    {
                        handleThrowable(it)
                    }
                )
        }
    }

    fun showAcceptSuccess() {
        showIncoming.value = false
        showOutgoging.value = true
        title.value = R.string.avchat_connecting

    }

    fun showIncoming() {
        showIncoming.value = true
        showOutgoging.value = false
        title.value = R.string.avchat_audio_call_request

    }

    fun showOutgoging() {
        showIncoming.value = false
        showOutgoging.value = true
        title.value = R.string.avchat_wait_recieve
        doCalling()
    }


    fun loadUser() {
        useCaseGetUserInfo.execute(arg.account)
            .autoDispose(this)
            .subscribe(
                {
                    val user = it.orNull() ?: return@subscribe
                    updateUser(user)
                },
                {
                    handleThrowable(it)
                }
            )
    }

    private fun updateUser(user: NimUserInfo) {
        avatar.value = user.avatar
    }

    fun doCalling() {
        useCaseAudioCalling.execute(arg.account)
            .autoDispose(this)
            .subscribe(
                {
                    arg.data = it.orNull()
                },
                {
                    handleThrowable(it)
                }
            )
    }


}