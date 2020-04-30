package com.netease.nim.demo.nim.audio

import android.content.Context
import com.hiwitech.android.shared.ext.logi
import com.netease.nim.demo.ui.message.main.viewmodel.ItemViewModelAudioMessage
import com.netease.nimlib.sdk.media.player.AudioPlayer
import com.netease.nimlib.sdk.media.player.OnPlayListener

class NimAudioManager(
    val context: Context
) : OnPlayListener {

    private var audioPlayer: AudioPlayer = AudioPlayer(context)

    private var itemViewModel: ItemViewModelAudioMessage? = null

    init {
        audioPlayer.onPlayListener = this
    }

    /**
     * 播放音频
     * @param itemViewModel 音频消息ItemViewModel
     * @param stream 音频播放模式 AudioManager.STREAM_VOICE_CALL/AudioManager.STREAM_MUSIC 听筒/扬声器
     */
    fun startAudio(itemViewModel: ItemViewModelAudioMessage, stream: Int) {
        this.itemViewModel = itemViewModel
        if (audioPlayer.isPlaying) {
            audioPlayer.stop()
        }
        audioPlayer.setDataSource(itemViewModel.attachment.pathForSave)
        audioPlayer.start(stream)
    }

    /**
     * 停止音频播放
     */
    fun stopAudio() {
        if (audioPlayer.isPlaying) {
            audioPlayer.stop()
        }
    }

    override fun onPrepared() {
        "onPrepared".logi()
    }

    override fun onCompletion() {
        "onCompletion".logi()
    }

    override fun onInterrupt() {
        "onInterrupt".logi()
    }

    override fun onError(error: String) {
        "onError:".plus(error).logi()
    }

    override fun onPlaying(duration: Long) {
        "onPlaying:".plus(duration).logi()
    }

}