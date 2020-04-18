package com.netease.nim.demo.ui.message.emoticon.viewmodel

import androidx.databinding.ObservableArrayList
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
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

    val onClickEmojiEvent = SingleLiveEvent<String>()

    val items = ObservableArrayList<Any>().apply {
        add(ItemViewModelEmojiPage(this@ViewModelEmoticon))
    }

    val pageItem = OnItemBindClass<Any>().apply {
        map<ItemViewModelEmojiPage>(BR.item, R.layout.item_emoticon_page_emoji)
    }

}