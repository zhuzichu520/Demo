package com.netease.nim.demo.ui.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.shared.ext.autoLoading
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetUserInfo
import com.netease.nim.demo.ui.user.arg.ArgUser
import com.uber.autodispose.autoDispose
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 11:40 AM
 * since: v 1.0.0
 */
class ViewModelUser @Inject constructor(
    private val useCaseGetUserInfo: UseCaseGetUserInfo
) : ViewModelBase<ArgUser>() {

    val items = MutableLiveData<List<Any>>()

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelUserHeader>(BR.item, R.layout.item_user_header)
        map<ItemViewModelUserSection>(BR.item, R.layout.item_user_section)
    }

    fun loadUserInfo() {
        useCaseGetUserInfo.execute(arg.account)
            .autoLoading(this)
            .autoDispose(this)
            .subscribe {
                items.value= listOf(
                    ItemViewModelUserHeader(this,it)
                )
            }
    }

}