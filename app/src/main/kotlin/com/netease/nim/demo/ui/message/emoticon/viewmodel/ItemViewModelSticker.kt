package com.netease.nim.demo.ui.message.emoticon.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.bus.RxBus
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.nim.emoji.StickerItem
import com.netease.nim.demo.nim.emoji.StickerManager
import com.netease.nim.demo.ui.message.emoticon.event.EventEmoticon

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/22 2:50 PM
 * since: v 1.0.0
 */
data class ItemViewModelSticker(
    val viewModel: BaseViewModel<*>,
    val stickerItem: StickerItem
) : ItemViewModelBase(viewModel) {

    val icon = MutableLiveData<Any>().apply {
        value = StickerManager.getInstance().getStickerUri(stickerItem.category, stickerItem.name)
    }

    val onClickStickerCommand = createCommand {
        RxBus.post(EventEmoticon.OnClickStickerEvent(stickerItem))
    }

}