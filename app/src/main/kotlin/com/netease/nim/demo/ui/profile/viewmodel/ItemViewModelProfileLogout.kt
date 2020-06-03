package com.netease.nim.demo.ui.profile.viewmodel

import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 3:18 PM
 * since: v 1.0.0
 */
class ItemViewModelProfileLogout(viewModel: ViewModelProfile) : ItemViewModelBase(viewModel) {

    val onClickExitCommand = createCommand {
        viewModel.onLogOutEvent.call()
    }

}