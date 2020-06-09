package com.netease.nim.demo.nim.audio

import android.content.Context
import com.hiwitech.android.shared.bus.LiveDataBus
import com.hiwitech.android.shared.ext.logi
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.message.main.viewmodel.ItemViewModelAudioMessage
import com.netease.nimlib.sdk.media.player.AudioPlayer
import com.netease.nimlib.sdk.media.player.OnPlayListener

class NimAudioManager(
    val context: Context
) : OnPlayListener {

    companion object {
        const val TYPE_DEFAULT = 0
        const val TYPE_PLAYING = 1
    }

    private var audioPlayer: AudioPlayer = AudioPlayer(context)

    private lateinit var itemViewModelAudioMessage: ItemViewModelAudioMessage

    init {
        audioPlayer.onPlayListener = this
    }

    /**
     * 播放音频
     * @param message 音频消息ItemViewModel
     * @param stream 音频播放模式 AudioManager.STREAM_VOICE_CALL/AudioManager.STREAM_MUSIC 听筒/扬声器
     */
    fun startAudio(itemViewModelAudioMessage: ItemViewModelAudioMessage, stream: Int) {
        if (audioPlayer.isPlaying) {
            audioPlayer.stop()
        }
        this.itemViewModelAudioMessage = itemViewModelAudioMessage
        audioPlayer.setDataSource((itemViewModelAudioMessage.attachment).pathForSave)
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
        LiveDataBus.post(NimEvent.OnAudioPlayEvent(itemViewModelAudioMessage, TYPE_PLAYING))
    }

    override fun onCompletion() {
        "onCompletion".logi()
        LiveDataBus.post(NimEvent.OnAudioPlayEvent(itemViewModelAudioMessage, TYPE_DEFAULT))
    }

    override fun onInterrupt() {
        "onInterrupt".logi()
        LiveDataBus.post(NimEvent.OnAudioPlayEvent(itemViewModelAudioMessage, TYPE_DEFAULT))
    }

    override fun onError(error: String) {
        "onError:".plus(error).logi()
        LiveDataBus.post(NimEvent.OnAudioPlayEvent(itemViewModelAudioMessage, TYPE_DEFAULT))
    }

    override fun onPlaying(duration: Long) {
        "onPlaying:".plus(duration).logi()
    }

}