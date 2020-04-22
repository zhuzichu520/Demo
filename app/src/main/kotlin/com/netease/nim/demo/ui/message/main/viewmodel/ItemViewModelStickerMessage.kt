package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.nim.attachment.StickerAttachment
import com.netease.nim.demo.nim.emoji.StickerManager
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 自定义消息-贴图消息
 * author: 朱子楚
 * time: 2020/4/22 8:28 PM
 * since: v 1.0.0
 */
class ItemViewModelStickerMessage(message: IMMessage) : ItemViewModelBaseMessage(message) {

    val sticker = MutableLiveData<Any>().apply {
        (message.attachment as? StickerAttachment)?.let {
            value = StickerManager.getInstance().getStickerUri(it.catalog, it.chartlet)
        }
    }
}