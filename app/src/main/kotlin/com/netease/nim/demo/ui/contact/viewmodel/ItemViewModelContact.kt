package com.netease.nim.demo.ui.contact.viewmodel

import androidx.lifecycle.MutableLiveData
import com.github.promeg.pinyinhelper.Pinyin
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.ui.user.ActivityUser
import com.netease.nim.demo.ui.user.arg.ArgUser
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 5:40 PM
 * since: v 1.0.0
 */
class ItemViewModelContact(
    viewModel: BaseViewModel<*>,
    nimUserInfo: NimUserInfo
) : ItemViewModelBase(viewModel) {

    val pinYin = Pinyin.toPinyin(nimUserInfo.name[0])[0].toUpperCase()

    /**
     * 头像
     */
    val avatar = MutableLiveData<Any>().apply {
        value = nimUserInfo.avatar
    }

    /**
     * 头像错误图片
     */
    val error = MutableLiveData<Int>(R.mipmap.nim_avatar_default)

    /**
     * 头像占位图
     */
    val placeholder = MutableLiveData<Int>(R.mipmap.nim_avatar_default)

    /**
     *
     */
    val title = MutableLiveData<String>().apply {
        value = nimUserInfo.name
    }

    val onClickCommand = createCommand {
        startActivity(ActivityUser::class.java, arg = ArgUser(nimUserInfo.account))
    }

}