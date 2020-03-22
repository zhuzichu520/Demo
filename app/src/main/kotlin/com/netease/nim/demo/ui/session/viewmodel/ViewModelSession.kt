package com.netease.nim.demo.ui.session.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.R
import com.netease.nim.demo.BR
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.session.domain.UseCaseGetSessionList
import com.uber.autodispose.autoDispose
import com.zhuzichu.android.mvvm.base.ArgDefault
import com.zhuzichu.android.shared.ext.autoLoading
import com.zhuzichu.android.shared.ext.map
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

class ViewModelSession @Inject constructor(
    private val useCaseGetSessionList: UseCaseGetSessionList
) : ViewModelBase<ArgDefault>() {

    val items = MutableLiveData<List<Any>>()

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelSession>(BR.item, R.layout.item_session)
    }

    fun loadSessionList() {
        useCaseGetSessionList.execute(Unit)
            .autoLoading(this)
            .autoDispose(this)
            .subscribe(
                {
                    items.value = it.map { item ->
                        ItemViewModelSession(this@ViewModelSession, item)
                    }
                },
                {
                    handleThrowable(it)
                }
            )
    }
}