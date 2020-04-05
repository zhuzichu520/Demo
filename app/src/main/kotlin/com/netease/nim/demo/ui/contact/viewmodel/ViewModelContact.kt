package com.netease.nim.demo.ui.contact.viewmodel

import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.storage.NimUserStorage
import javax.inject.Inject

/**
 * desc 通讯录ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelContact @Inject constructor() : ViewModelBase<ArgDefault>() {

    val onClickExitCommand = createCommand {
        NimUserStorage.logout()
    }

}