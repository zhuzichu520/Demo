package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.libs.tool.isFileExists
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.R
import com.netease.nim.demo.nim.audio.NimAudioManager
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.message.main.domain.UseCaseDowloadAttachment
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/27 10:19 PM
 * since: v 1.0.0
 */
class ItemViewModelAudioMessage(
    viewModel: BaseViewModel<*>,
    message: IMMessage,
    useCaseDowloadAttachment: UseCaseDowloadAttachment,
    private val nimAudioManager: NimAudioManager
) : ItemViewModelBaseMessage(viewModel, message) {

    val attachment = message.attachment as AudioAttachment

    /**
     * audio图标
     */
    val audioIcon = MutableLiveData<Int>().apply {
        value = if (isMine()) {
            R.drawable.nim_audio_animation_list_right_3
        } else {
            R.drawable.nim_audio_animation_list_left_3
        }
    }


    /**
     * 音频时长
     */
    val duration = MutableLiveData<String>().apply {
        value = (attachment.duration / 1000).toString().plus("\"")
    }

    val onClickAudioCommand = createCommand {
        val isExist = isFileExists(attachment.pathForSave)
        if (!isExist) {
            useCaseDowloadAttachment.execute(
                UseCaseDowloadAttachment.Parameters(message, false)
            ).subscribe {
                startAudio()
            }
        } else {
            startAudio()
        }
    }

    private fun startAudio() {
        nimAudioManager.startAudio(this, NimUserStorage.audioStream)
    }

    private fun stopAudio() {
        nimAudioManager.stopAudio()
    }

}