package com.netease.nim.demo.ui.file.event

import java.io.File

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/9 11:17 AM
 * since: v 1.0.0
 */
class EventFile {
    data class OnSendFileEvent(
        val file: File
    )
}