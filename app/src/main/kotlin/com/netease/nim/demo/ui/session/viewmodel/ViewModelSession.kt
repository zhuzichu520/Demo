package com.netease.nim.demo.ui.session.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.common.primitives.Longs
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.session.Constants.SESSION_ON_TOP
import com.netease.nim.demo.ui.session.domain.UseCaseGetSessionList
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.uber.autodispose.autoDispose
import com.zhuzichu.android.libs.tool.replaceAt
import com.zhuzichu.android.libs.tool.toCast
import com.zhuzichu.android.mvvm.base.ArgDefault
import com.zhuzichu.android.shared.ext.autoLoading
import com.zhuzichu.android.shared.ext.map
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

class ViewModelSession @Inject constructor(
    private val useCaseGetSessionList: UseCaseGetSessionList,
    val msgService: MsgService
) : ViewModelBase<ArgDefault>() {

    val sessionList = MutableLiveData<List<ItemViewModelSession>>()

    val items: LiveData<List<Any>> =
        Transformations.map<List<ItemViewModelSession>, List<Any>>(sessionList) { input ->
            val list = ArrayList<Any>()
            list.addAll(input)
            list
        }

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelSession>(BR.item, R.layout.item_session)
    }

    private
    val comparator: Comparator<ItemViewModelSession> = Comparator { left, right ->
        val mapRight = right.contact.extension ?: mapOf()
        val mapLeft = left.contact.extension ?: mapOf()
        val longRight: Long = (mapRight[SESSION_ON_TOP] ?: Long.MIN_VALUE).toCast()
        val longLeft: Long = (mapLeft[SESSION_ON_TOP] ?: Long.MIN_VALUE).toCast()
        //先比较置顶 后 比较时间
        if (longLeft != Long.MIN_VALUE && longRight == Long.MIN_VALUE) {
            -1
        } else if (longLeft == Long.MIN_VALUE && longRight != Long.MIN_VALUE) {
            1
        } else {
            Longs.compare(right.time.coerceAtLeast(longRight), left.time.coerceAtLeast(longLeft))
        }
    }


    fun loadSessionList() {
        useCaseGetSessionList.execute(Unit)
            .autoLoading(this)
            .autoDispose(this)
            .subscribe(
                {
                    parseSessionList(it)
                },
                {
                    handleThrowable(it)
                }
            )
    }

    fun parseSessionList(list: List<RecentContact>) {
        sessionList.value?.apply {
            var data = this
            list.forEach { item ->
                val session = ItemViewModelSession(this@ViewModelSession, item)
                val position = data.indexOf(session)
                data = if (position == -1) {
                    data.plus(session)
                } else {
                    data.replaceAt(position) {
                        it.copy(viewModel = this@ViewModelSession, contact = item)
                    }
                }
            }
            sessionList.value = data.sortedWith(comparator)
        } ?: apply {
            sessionList.value = list.map { item ->
                ItemViewModelSession(this@ViewModelSession, item)
            }.sortedWith(comparator)
        }
    }

    fun refresh() {
        sessionList.value?.let {
            sessionList.value = it.sortedWith(comparator)
        }
    }


    fun refresh(item: ItemViewModelSession) {
        sessionList.value?.let {
            sessionList.value = it.replaceAt(it.indexOf(item)) { item ->
                item.copy(viewModel = item.viewModel, contact = item.contact)
            }
        }
    }

}