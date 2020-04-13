package com.netease.nim.demo.ui.message.more.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.netease.nim.demo.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/13 3:59 PM
 * since: v 1.0.0
 */
data class ItemViewModelMore(
    private val viewModel: BaseViewModel<*>,
    @StringRes private val titleId: Int,
    @DrawableRes private val iconId: Int
) : ItemViewModelBase(viewModel) {
    val title = MutableLiveData(titleId)
    val icon = MutableLiveData(iconId)
}