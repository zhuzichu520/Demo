package com.netease.nim.demo.ui.dialog.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

/**
 * desc 个人中心ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelOptions @Inject constructor(
) : ViewModelBase<ArgDefault>() {

    val items = MutableLiveData<List<ItemViewModelOptions>>()

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelOptions>(BR.item, R.layout.item_options)
    }

    val onDialogDismissEvent = SingleLiveEvent<Unit>()
}