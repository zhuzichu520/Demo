package com.netease.nim.demo.ui.message.emoticon.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.nim.emoji.StickerCategory
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/15 6:06 PM
 * since: v 1.0.0
 */
data class ItemViewModelStickerPage(
    val viewModel: BaseViewModel<*>,
    val stickerCategory: StickerCategory
) : ItemViewModelBase(viewModel) {

    val items = MutableLiveData<List<Any>>().apply {
        val stickers = stickerCategory.stickers
        value = stickers.map {
            ItemViewModelSticker(viewModel,it)
        }
    }

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelSticker>(BR.item, R.layout.item_sticker)
    }

}