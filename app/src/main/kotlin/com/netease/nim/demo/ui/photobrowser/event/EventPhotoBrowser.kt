package com.netease.nim.demo.ui.photobrowser.event

import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/12 4:31 PM
 * since: v 1.0.0
 */
class EventPhotoBrowser {
    data class OnUpdateEnterSharedElement(
        val message: IMMessage
    )
}