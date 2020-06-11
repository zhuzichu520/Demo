package com.netease.nim.demo.ui.profile.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.toStringByResId
import com.netease.nim.demo.R
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
    val text: String?,
    onClickEvent: SingleLiveEvent<ItemViewModelProfileEdit>
) : ItemViewModelBase(viewModel) {

    val title = MutableLiveData<Int>(title)

    val content = MutableLiveData<String>().apply {
        //是好友，显示备注名
        val alias = if (text.isNullOrEmpty())
            R.string.no_setting.toStringByResId()
        else
            text
        value = alias
    }

    val onClickCommand = createCommand {
        onClickEvent.value = this
    }

}