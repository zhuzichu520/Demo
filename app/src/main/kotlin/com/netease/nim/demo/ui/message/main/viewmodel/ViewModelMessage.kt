package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.map
import com.hiwitech.android.shared.widget.page.PageHelper
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetMessageList
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetTeamInfo
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetUserInfo
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.uber.autodispose.autoDispose
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
    private val useCaseGetMessageList: UseCaseGetMessageList
) : ViewModelBase<ArgMessage>() {

    private val pageSize = 30

    /**
     * 滑动事件
     */
    val onScrollPositionsEvent = SingleLiveEvent<Int>()

    /**
     * 标题
     */
    val title = MutableLiveData<String>()

    /**
     * 消息数据
     */
    private val messageList = MutableLiveData<List<ItemViewModelBaseMessage>>()

    /**
     * 分页
     */
    private val pageHelper = PageHelper(
        viewModel = this,
        items = messageList,
        pageSize = pageSize,
        onRefresh = {
            messageList.value?.let {
                loadMessage(it[0].message)
            }
        })

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
                    val list = it.map { item ->
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
                    val data = pageHelper.add(list, true)
                    if (true == isFirst) {
                        onScrollPositionsEvent.value = data.size - 1
                    } else {
                        onScrollPositionsEvent.value = list.size + 1
                    }
                },
                {
                    handleThrowable(it)
                }
            )
    }


}