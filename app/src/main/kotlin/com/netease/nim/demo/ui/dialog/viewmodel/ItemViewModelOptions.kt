package com.netease.nim.demo.ui.dialog.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.ui.dialog.entity.EntityOptions

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/26 9:54 AM
 * since: v 1.0.0
 */
class ItemViewModelOptions(
    viewModel: ViewModelOptions,
    options: EntityOptions
) : ItemViewModelBase(viewModel) {

    val title=MutableLiveData<Int>(options.title)

    val onClickItemCommand = createCommand {
        options.onClickItemEvent.value = options
        viewModel.onDialogDismissEvent.call()
    }

}