package com.netease.nim.demo.ui.multiport.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.multiport.arg.ArgMultiport
import com.netease.nim.demo.ui.multiport.domain.UseCaseKickOtherOut
import com.netease.nimlib.sdk.auth.OnlineClient
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

/**
 * desc 注册ViewModeel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelMultiport @Inject constructor(
    private val useCaseKickOtherOut: UseCaseKickOtherOut
) : ViewModelBase<ArgMultiport>() {


    val items = MutableLiveData<List<Any>>()

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelMultiportOption>(BR.item, R.layout.item_multiport_option)
    }

    fun updateOnlienClent(list: List<OnlineClient>) {
        items.value = list.map {
            ItemViewModelMultiportOption(this, it, useCaseKickOtherOut)
        }
    }


}