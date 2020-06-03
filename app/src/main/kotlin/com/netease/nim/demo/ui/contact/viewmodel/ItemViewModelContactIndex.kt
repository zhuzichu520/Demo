package com.netease.nim.demo.ui.contact.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.netease.nim.demo.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 5:40 PM
 * since: v 1.0.0
 */
class ItemViewModelContactIndex(
    viewModel: BaseViewModel<*>,
    val letter: String
) : ItemViewModelBase(viewModel) {

    val title = MutableLiveData<String>().apply {
        value = letter
    }

}