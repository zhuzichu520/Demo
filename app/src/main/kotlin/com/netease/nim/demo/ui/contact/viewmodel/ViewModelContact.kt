package com.netease.nim.demo.ui.contact.viewmodel

import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.storage.NimUserStorage
import com.zhuzichu.android.mvvm.base.ArgDefault
import com.zhuzichu.android.shared.ext.createCommand
import javax.inject.Inject

class ViewModelContact @Inject constructor() : ViewModelBase<ArgDefault>() {

    val onClickExit = createCommand {
        NimUserStorage.logout()
    }

}