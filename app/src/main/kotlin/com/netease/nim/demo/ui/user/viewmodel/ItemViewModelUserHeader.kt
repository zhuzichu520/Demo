package com.netease.nim.demo.ui.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/3 2:51 PM
 * since: v 1.0.0
 */
class ItemViewModelUserHeader(
    viewModel: BaseViewModel<*>,
    nimUserInfo: NimUserInfo
) : ItemViewModelBase(viewModel) {

    /**
     * 头像
     */
    val avatar = MutableLiveData<Any>(nimUserInfo.avatar)

    /**
     * 头像错误图片
     */
    val error = MutableLiveData<Int>(R.mipmap.nim_avatar_default)

    /**
     * 头像占位图
     */
    val placeholder = MutableLiveData<Int>(R.mipmap.nim_avatar_default)


    val account = MutableLiveData<String>().apply {
        value = "账号：".plus(nimUserInfo.account)
    }

    val name = MutableLiveData<String>().apply {
        value = nimUserInfo.name
    }

    val gender = MutableLiveData<Int>().apply {
        value = when (nimUserInfo.genderEnum) {
            GenderEnum.FEMALE -> {
                R.drawable.nim_female
            }
            GenderEnum.MALE -> {
                R.drawable.nim_male
            }
            else -> {
                R.drawable.transparent
            }
        }
    }

}