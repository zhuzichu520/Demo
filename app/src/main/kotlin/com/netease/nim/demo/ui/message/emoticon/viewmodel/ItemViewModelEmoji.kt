package com.netease.nim.demo.ui.message.emoticon.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.bus.RxBus
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.global.AppGlobal.context
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.nim.emoji.EmojiManager
import com.netease.nim.demo.ui.message.emoticon.event.EventEmoticon

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/15 6:26 PM
 * since: v 1.0.0
 */
data class ItemViewModelEmoji(
    val viewModel: BaseViewModel<*>,
    internal var index: Int
) : ItemViewModelBase(viewModel) {

    val text = EmojiManager.getDisplayText(index)

    val darawble = MutableLiveData<Drawable>().apply {
        value = EmojiManager.getDisplayDrawable(context, index)
    }

    val onClickEmojiCommand = createCommand {
        RxBus.post(EventEmoticon.OnClickEmojiEvent(text))
    }

}