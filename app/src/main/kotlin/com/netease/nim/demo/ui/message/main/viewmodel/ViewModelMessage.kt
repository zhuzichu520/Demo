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

class ViewModelMessage @Inject constructor(
    private val useCaseGetTeamInfo: UseCaseGetTeamInfo,
    private val useCaseGetUserInfo: UseCaseGetUserInfo,
    private val useCaseGetMessageList: UseCaseGetMessageList
) : ViewModelBase<ArgMessage>() {

    private val pageSize = 30

    val onScrollPositionsEvent = SingleLiveEvent<Int>()

    val title = MutableLiveData<String>()

    private val messageList = MutableLiveData<List<ItemViewModelBaseMessage>>()

    val pageHelper = PageHelper(
        viewModel = this,
        items = messageList,
        pageSize = pageSize,
        onRefresh = {
            messageList.value?.let {
                loadMessage(it[0].message)
            }
        })

    val onRefreshCommand = pageHelper.onRefreshCommand

    val items = pageHelper.pageItems

    val itemBinding = pageHelper.pageItemBinding.apply {
        map<ItemViewModelTextMessage>(BR.item, R.layout.item_message_text)
        map<ItemViewModelPictureMessage>(BR.item, R.layout.item_message_picture)
        map<ItemViewModelUnknownMessage>(BR.item, R.layout.item_message_unknown)
    }

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

    fun loadMessage(
        anchor: IMMessage,
        isFirst: Boolean? = false
    ) {
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