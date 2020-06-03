package com.netease.nim.demo.ui.profile.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 2:37 PM
 * since: v 1.0.0
 */
class ItemViewModelProfileHeader(
    viewModel: BaseViewModel<*>,
    avatar: String
) : ItemViewModelBase(viewModel) {

    /**
     * 头像
     */
    val avatar = MutableLiveData<Any>(avatar)

    /**
     * 头像错误图片
     */
    val error = MutableLiveData<Int>(R.mipmap.nim_avatar_default)

    /**
     * 头像占位图
     */
    val placeholder = MutableLiveData<Int>(R.mipmap.nim_avatar_default)


}