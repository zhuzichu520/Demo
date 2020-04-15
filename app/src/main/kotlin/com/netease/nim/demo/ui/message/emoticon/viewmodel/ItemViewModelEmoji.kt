package com.netease.nim.demo.ui.message.emoticon.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.toast
import com.hiwitech.android.shared.global.AppGlobal.context
import com.netease.nim.demo.nim.emoji.EmojiManager

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/15 6:26 PM
 * since: v 1.0.0
 */
data class ItemViewModelEmoji(
    internal var index: Int
) {

    val darawble = MutableLiveData<Drawable>().apply {
        value = EmojiManager.getDisplayDrawable(context,index)
    }

    val onClickEmoji= createCommand {
        "哈哈".toast()
    }

}