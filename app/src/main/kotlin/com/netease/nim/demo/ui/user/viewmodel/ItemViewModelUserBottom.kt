package com.netease.nim.demo.ui.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.mvvm.databinding.BindingCommand
import com.netease.nim.demo.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/9 8:01 PM
 * since: v 1.0.0
 */
class ItemViewModelUserBottom(
    viewModel: BaseViewModel<*>,
    val onClickChatCommand: BindingCommand<Any>,
    val onClickDeleteCommand: BindingCommand<Any>,
    val onClickAddCommand: BindingCommand<Any>
) : ItemViewModelBase(viewModel) {

    val delete = MutableLiveData<Boolean>(false)

    val add = MutableLiveData<Boolean>(false)

    fun showAdd() {
        add.value = true
        delete.value = false
    }

    fun showDelete() {
        add.value = false
        delete.value = true
    }
}