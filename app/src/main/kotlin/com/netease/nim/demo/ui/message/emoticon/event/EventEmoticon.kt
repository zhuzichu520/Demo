package com.netease.nim.demo.ui.message.emoticon.event

import com.netease.nim.demo.nim.emoji.StickerItem

/**
 * desc Emoticon相关的事件
 * author: 朱子楚
 * time: 2020/4/21 3:43 PM
 * since: v 1.0.0
 */
class EventEmoticon {
    /**
     * 点击Emoji事件
     */
    data class OnClickEmojiEvent(
        val text: String
    )

    /**
     * 点击贴图事件
     */
    data class OnClickStickerEvent(
        val stickerItem: StickerItem
    )
}