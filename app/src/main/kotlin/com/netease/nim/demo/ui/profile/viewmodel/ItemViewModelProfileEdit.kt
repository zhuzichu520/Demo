package com.netease.nim.demo.ui.profile.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.netease.nim.demo.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 2:35 PM
 * since: v 1.0.0
 */
class ItemViewModelProfileEdit(
    viewModel: BaseViewModel<*>,
    @StringRes title: Int,
    content: String
) : ItemViewModelBase(viewModel) {

    val title = MutableLiveData<Int>(title)

    val content = MutableLiveData<String>(content)


}