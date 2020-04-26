package com.netease.nim.demo.ui.message.main.event

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/24 11:34 AM
 * since: v 1.0.0
 */
class EventMessage {

    companion object {
        //录制取消
        const val RECORD_CANCEL = 0
        //录完可以发送
        const val RECORD_SEND = 1
        //正在录制中
        const val RECORD_RECORDING = 2
    }

    data class OnRecordAudioEvent(
        val recordType: Int,
        val recordSecond: Int
    )

    data class OnRecordCancelChangeEvent(
        val cancelled: Boolean
    )
}