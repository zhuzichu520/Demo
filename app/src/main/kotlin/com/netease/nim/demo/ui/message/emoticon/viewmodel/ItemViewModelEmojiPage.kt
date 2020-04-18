package com.netease.nim.demo.ui.message.emoticon.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.nim.emoji.EmojiManager
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/15 6:06 PM
 * since: v 1.0.0
 */
class ItemViewModelEmojiPage(
    viewModel: BaseViewModel<*>
) : ItemViewModelBase(viewModel) {

    val items = MutableLiveData<List<Any>>().apply {
        val list = mutableListOf<ItemViewModelEmoji>()
        for (i in 0 until EmojiManager.getDisplayCount()) {
            list.add(ItemViewModelEmoji(viewModel, i))
        }
        value = list
    }

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelEmoji>(BR.item, R.layout.item_emoji)
    }

}