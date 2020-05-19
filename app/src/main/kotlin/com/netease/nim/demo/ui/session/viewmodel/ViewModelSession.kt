package com.netease.nim.demo.ui.session.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.common.primitives.Longs
import com.hiwitech.android.libs.tool.replaceAt
import com.hiwitech.android.libs.tool.toCast
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.ext.autoLoading
import com.hiwitech.android.shared.ext.itemDiffOf
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.session.Constants.SESSION_ON_TOP
import com.netease.nim.demo.ui.session.domain.UseCaseGetSessionList
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.uber.autodispose.autoDispose
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

/**
 * desc 消息列表ViewModel
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
class ViewModelSession @Inject constructor(
    private val useCaseGetSessionList: UseCaseGetSessionList,
    val msgService: MsgService
) : ViewModelBase<ArgDefault>() {

    /**
     * 会话列表的会话数据
     */
    val sessionList = MutableLiveData<List<ItemViewModelSession>>()

    /**
     * 会话列表所有数据
     */
    val items: LiveData<List<Any>> =
        Transformations.map<List<ItemViewModelSession>, List<Any>>(sessionList) { input ->
            val list = ArrayList<Any>()
            list.addAll(input)
            list
        }

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelSession>(BR.item, R.layout.item_session)
    }

    val diff = itemDiffOf<ItemViewModelSession> { oldItem, newItem ->
        oldItem.contactId == newItem.contactId && oldItem.messageId == newItem.messageId
                && oldItem.number.value == newItem.number.value
    }

    /**
     * 比较器
     */
    private val comparator: Comparator<ItemViewModelSession> = Comparator { left, right ->
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

    /**
     * 加载会话数据
     */
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

    /**
     * 处理消息数据
     * @param list 最近会话数据
     */
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

    /**
     * 重新刷新会话列表
     */
    fun refresh() {
        sessionList.value?.let {
            sessionList.value = it.sortedWith(comparator)
        }
    }

    /**
     * 删除某个会话
     * @param session 会话
     */
    fun deleteSession(session: ItemViewModelSession) {
        val contract = session.contact
        msgService.deleteRecentContact(contract)
        msgService.clearChattingHistory(contract.contactId, contract.sessionType)
        sessionList.value?.let {
            sessionList.value = it - session
        }
    }
}