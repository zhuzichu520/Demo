package com.netease.nim.demo.ui.me.main.viewmodel

import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.base.ViewModelBase
import javax.inject.Inject

/**
 * desc 个人中心ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelMe @Inject constructor(
) : ViewModelBase<ArgDefault>() {

    val onClickThemeEvent = SingleLiveEvent<Unit>()

    val onClickThemeCommand = createCommand {
        onClickThemeEvent.call()
    }

}