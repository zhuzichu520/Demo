package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.libs.tool.replaceAt
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.itemPageDiffOf
import com.hiwitech.android.shared.ext.map
import com.hiwitech.android.shared.widget.page.PageHelper
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetMessageList
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetTeamInfo
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetUserInfo
import com.netease.nim.demo.ui.message.main.domain.UseCaseSendMessage
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.uber.autodispose.autoDispose
import me.tatarka.bindingcollectionadapter2.collections.AsyncDiffObservableList
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList
import javax.inject.Inject

/**
 * desc 消息列表ViewMNodel
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
class ViewModelMessage @Inject constructor(
    private val useCaseGetTeamInfo: UseCaseGetTeamInfo,
    private val useCaseGetUserInfo: UseCaseGetUserInfo,
    private val useCaseGetMessageList: UseCaseGetMessageList,
    private val useCaseSendMessage: UseCaseSendMessage
) : ViewModelBase<ArgMessage>() {

    private val pageSize = 30

    /**
     * 滑动事件
     */
    val onScrollPositionsEvent = SingleLiveEvent<Int>()

    /**
     * 添加数据完成事件
     */
    val onAddMessageCompletedEvent = SingleLiveEvent<List<Any>>()

    /**
     * 标题
     */
    val title = MutableLiveData<String>()

    /**
     * 消息数据
     */
    private val messageList =
        AsyncDiffObservableList(itemPageDiffOf<ItemViewModelBaseMessage> { oldItem, newItem ->
            oldItem.uuid == newItem.uuid && oldItem.status == newItem.status
        })

    /**
     * 分页
     */
    private val pageHelper = PageHelper(
        viewModel = this,
        items = messageList,
        pageSize = pageSize,
        onRefresh = {
            loadMessage((messageList[0] as ItemViewModelBaseMessage).message)
        }
    )

    /**
     * 上拉刷新状态
     */
    val isRefresh = pageHelper.isRefresh

    /**
     * 刷新指令
     */
    val onRefreshCommand = pageHelper.onRefreshCommand


    /**
     * 数据列表
     */
    val items = pageHelper.pageItems

    /**
     * 多布局注册
     */
    val itemBinding = pageHelper.pageItemBinding.apply {
        map<ItemViewModelTextMessage>(BR.item, R.layout.item_message_text)
        map<ItemViewModelPictureMessage>(BR.item, R.layout.item_message_picture)
        map<ItemViewModelUnknownMessage>(BR.item, R.layout.item_message_unknown)
    }

    /**
     * 加载用户详情信息
     */
    fun loadUserInfo() {
        useCaseGetUserInfo.execute(arg.contactId)
            .autoDispose(this)
            .subscribe(
                {
                    title.value = it.name
                },
                {
                    handleThrowable(it)
                }
            )

    }

    /**
     * 发送消息
     */
    fun sendMessage(message: IMMessage, resend: Boolean = false) {
        useCaseSendMessage.execute(UseCaseSendMessage.Parameters(message, resend))
            .autoDispose(this)
            .subscribe {

            }
    }

    /**
     * 加载群组详情信息
     */
    fun loadTeamInfo() {
        useCaseGetTeamInfo.execute(arg.contactId)
            .autoDispose(this)
            .subscribe(
                {
                    title.value = it.name
                },
                {
                    handleThrowable(it)
                }
            )
    }

    /**
     * 加载消息数据
     *
     * @param anchor 查询开始消息
     * @param isFirst 是否第一次加载
     */
    fun loadMessage(anchor: IMMessage, isFirst: Boolean? = false) {
        useCaseGetMessageList.execute(UseCaseGetMessageList.Parameters(anchor, pageSize))
            .autoDispose(this)
            .subscribe(
                {
                    val list = handleMessageList(it)
                    val data = pageHelper.add(list, true)
                    if (true == isFirst) {
                        onScrollPositionsEvent.value = data.size - 1
                    }
                },
                {
                    handleThrowable(it)
                }
            )
    }

    /**
     * 添加新消息
     *
     * @param list 消息集合
     */
    fun addMessage(list: List<IMMessage>) {
        val data = handleMessageList(list)
        data.forEach {
            val index = messageList.lastIndexOf(it)
            if (index != -1) {
                //已经存在
                messageList.update(messageList.replaceAt(index) { _ -> it })
            } else {
                //不存在
                messageList.update(messageList + it)
            }
        }
        onAddMessageCompletedEvent.value = messageList
    }

    /**
     * 处理消息集合返回数据模型集合
     * @param list 消息集合
     */
    private fun handleMessageList(list: List<IMMessage>): List<ItemViewModelBaseMessage> {
        return list.map { item ->
            when (item.msgType) {
                MsgTypeEnum.text -> {
                    ItemViewModelTextMessage(item)
                }
                MsgTypeEnum.image -> {
                    ItemViewModelPictureMessage(item)
                }
                else -> {
                    ItemViewModelUnknownMessage(item)
                }
            }
        }
    }

}