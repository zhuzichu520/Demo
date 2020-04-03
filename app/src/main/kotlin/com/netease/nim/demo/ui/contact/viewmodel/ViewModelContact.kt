package com.netease.nim.demo.ui.contact.viewmodel

import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.storage.NimUserStorage
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.ext.createCommand
import javax.inject.Inject

class ViewModelContact @Inject constructor() : ViewModelBase<ArgDefault>() {

    val onClickExitCommand = createCommand {
        NimUserStorage.logout()
    }

}