package com.netease.nim.demo.ui.message.emoticon.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.nim.emoji.StickerManager
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/13 2:45 PM
 * since: v 1.0.0
 */
class ViewModelEmoticon @Inject constructor(
) : ViewModelBase<ArgDefault>() {

    val tabIndex = MutableLiveData<Int>()

    val pageItems = ObservableArrayList<Any>().apply {
        add(ItemViewModelEmojiPage(this@ViewModelEmoticon))
        val categories = StickerManager.getInstance().categories
        categories.forEach {
            add(ItemViewModelStickerPage(this@ViewModelEmoticon, it))
        }
    }

    val pageItemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelEmojiPage>(BR.item, R.layout.item_emoticon_page_emoji)
        map<ItemViewModelStickerPage>(BR.item, R.layout.item_emoticon_page_sticker)
    }

    val tabItemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelTab>(BR.item, R.layout.item_emoticon_tab)
    }

    val tabItems = ObservableArrayList<Any>().apply {
        var i = 0
        add(
            ItemViewModelTab(
                this@ViewModelEmoticon,
                R.mipmap.nim_emoji_normal,
                R.mipmap.nim_emoji_pressed,
                i++
            )
        )
        val categories = StickerManager.getInstance().categories
        categories.forEach {
            add(ItemViewModelTab(this@ViewModelEmoticon, it.normalUri, it.pressedlUri, i++))
        }
    }

    fun updateTabs() {
        tabItems.forEachIndexed { index, any ->
            (any as? ItemViewModelTab)?.let {
                if (index == tabIndex.value) {
                    it.icon.value = it.pressed
                } else {
                    it.icon.value = it.normal
                }
            }
        }
    }

}