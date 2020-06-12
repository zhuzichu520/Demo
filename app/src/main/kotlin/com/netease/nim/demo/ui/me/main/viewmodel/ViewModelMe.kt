package com.netease.nim.demo.ui.me.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetUserInfo
import com.netease.nim.demo.ui.profile.ActivityProfile
import com.netease.nim.demo.ui.setting.ActivitySetting
import com.uber.autodispose.autoDispose
import javax.inject.Inject

/**
 * desc 个人中心ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelMe @Inject constructor(
    private val useCaseGetUserInfo: UseCaseGetUserInfo
) : ViewModelBase<ArgDefault>() {

    /**
     * 头像
     */
    val avatar = MutableLiveData<Any>()

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
    val nickname = MutableLiveData<String>()

    /**
     *
     */
    val account = MutableLiveData<String>()

    /**
     *
     */
    val onClickHeaderCommand = createCommand {
        startActivity(ActivityProfile::class.java)
    }

    val onClickSettingCommand = createCommand {
        startActivity(ActivitySetting::class.java)
    }

    val onClickThemeEvent = SingleLiveEvent<Unit>()

    val onClickThemeCommand = createCommand {
        onClickThemeEvent.call()
    }

    fun updateUserInfo() {
        useCaseGetUserInfo.execute(NimUserStorage.account.toString()).autoDispose(this)
            .subscribe {
                it.orNull()?.let { userInfo ->
                    avatar.value = userInfo.avatar
                    nickname.value = userInfo.name
                    account.value = "账号：".plus(userInfo.account)
                }
            }
    }

}