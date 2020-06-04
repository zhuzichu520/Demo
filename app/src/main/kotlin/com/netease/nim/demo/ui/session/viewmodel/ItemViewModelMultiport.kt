package com.netease.nim.demo.ui.session.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.mvvm.databinding.BindingCommand
import com.netease.nim.demo.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/4 11:42 AM
 * since: v 1.0.0
 */
class ItemViewModelMultiport(
    viewModel: BaseViewModel<*>
) : ItemViewModelBase(viewModel) {

    val title = MutableLiveData<String>()

    val state = MutableLiveData(1)

    val onClickCommand = MutableLiveData<BindingCommand<Any>>()

}