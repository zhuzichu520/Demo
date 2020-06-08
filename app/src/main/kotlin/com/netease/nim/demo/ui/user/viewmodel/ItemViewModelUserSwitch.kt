package com.netease.nim.demo.ui.user.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.netease.nim.demo.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/3 2:55 PM
 * since: v 1.0.0
 */
class ItemViewModelUserSwitch(
    viewModel: BaseViewModel<*>,
    @StringRes title: Int,
    checked: Boolean
) : ItemViewModelBase(viewModel) {

    val title = MutableLiveData<Int>(title)

    val checked = MutableLiveData<Boolean>(checked)

}