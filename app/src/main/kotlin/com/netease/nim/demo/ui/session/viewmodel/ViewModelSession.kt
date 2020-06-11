package com.netease.nim.demo.ui.session.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.common.primitives.Longs
import com.hiwitech.android.libs.tool.replaceAt
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.mvvm.databinding.BindingCommand
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.nim.tools.ToolSticky
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

    private val itemViewModelNetwork = ItemViewModelNetwork(this)

    private val itemViewModelMultiport = ItemViewModelMultiport(this)

    /**
     * 会话列表所有数据
     */
    val items: LiveData<List<Any>> =
        Transformations.map<List<ItemViewModelSession>, List<Any>>(sessionList) { input ->
            val list = ArrayList<Any>()
            list.add(itemViewModelNetwork)
            list.add(itemViewModelMultiport)
            list.addAll(input)
            list
        }

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelSession>(BR.item, R.layout.item_session)
        map<ItemViewModelNetwork>(BR.item, R.layout.item_network)
        map<ItemViewModelMultiport>(BR.item, R.layout.item_multiport)
    }

    /**
     * Item长点击
     */
    val onLongClickItemEvent = SingleLiveEvent<ItemViewModelSession>()

    /**
     * 比较器
     */
    private val comparator: Comparator<ItemViewModelSession> = Comparator { left, right ->
        val longRight: Long = ToolSticky.getStickyLong(right.contact)
        val longLeft: Long = ToolSticky.getStickyLong(left.contact)
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
            .autoDispose(this)
            .subscribe(
                {
                    parseSessionList(it.get())
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
        var data = sessionList.value ?: listOf()
        list.forEach { item ->
            val session = ItemViewModelSession(this, item)
            val position = data.indexOf(session)
            data = if (position == -1) {
                data + session
            } else {
                data.replaceAt(position) {
                    session
                }
            }
        }
        sessionList.value = data.sortedWith(comparator)
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

    fun setNetWorkText(@StringRes textId: Int) {
        itemViewModelNetwork.title.value = textId
    }

    fun showNetWorkBar(isShown: Boolean) {
        itemViewModelNetwork.state.value = if (isShown) 0 else 1
    }

    fun setMultiportText(text: String) {
        itemViewModelMultiport.title.value = text
    }

    fun showMultiportBar(isShown: Boolean) {
        itemViewModelMultiport.state.value = if (isShown) 0 else 1
    }

    fun updateMultiportCommand(command: BindingCommand<Any>) {
        itemViewModelMultiport.onClickCommand.value = command
    }

    /**
     * 重新刷新会话列表
     */
    fun refresh(accounts: List<String>) {
        accounts.forEach { account ->
            var position = -1
            val list = sessionList.value ?: listOf()
            list.forEachIndexed { index, item ->
                if (item.contactId == account) {
                    position = index
                    return@forEachIndexed
                }
            }
            if (position != -1) {
                sessionList.value = list.replaceAt(position) {
                    it.copy(viewModel = this)
                }
            }
        }

    }

}