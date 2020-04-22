package com.netease.nim.demo.ui.message.emoticon.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/22 3:28 PM
 * since: v 1.0.0
 */
data class ItemViewModelTab(
    val viewModel: BaseViewModel<*>,
    val normal: Any,
    val pressed: Any,
    val index: Int
) : ItemViewModelBase(viewModel) {

    val icon = MutableLiveData<Any>(normal)

    val onClickTabCommand = createCommand {
        (viewModel as? ViewModelEmoticon)?.tabIndex?.value = index
    }

}