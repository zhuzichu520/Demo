package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.DiffUtil
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.createTypeCommand
import com.hiwitech.android.shared.ext.itemDiffOf
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetMessageList
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetTeamInfo
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetUserInfo
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.uber.autodispose.autoDispose
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

class ViewModelMessage @Inject constructor(
    private val useCaseGetTeamInfo: UseCaseGetTeamInfo,
    private val useCaseGetUserInfo: UseCaseGetUserInfo,
    private val useCaseGetMessageList: UseCaseGetMessageList
) : ViewModelBase<ArgMessage>() {

    private val pageSize = 40

    val onScrollPositionsEvent = SingleLiveEvent<Int>()

    val title = MutableLiveData<String>()

    val messageList = MutableLiveData<List<ItemViewModelBaseMessage>>()

    val items: LiveData<List<Any>> =
        Transformations.map<List<ItemViewModelBaseMessage>, List<Any>>(messageList) { input ->
            val list = ArrayList<Any>()
            list.addAll(input)
            list
        }

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelTextMessage>(BR.item, R.layout.item_message_text)
        map<ItemViewModelPictureMessage>(BR.item, R.layout.item_message_picture)
        map<ItemViewModelUnknownMessage>(BR.item, R.layout.item_message_unknown)
    }

    val diff: DiffUtil.ItemCallback<Any> =
        itemDiffOf<ItemViewModelBaseMessage> { oldItem, newItem ->
            oldItem.sessionId == newItem.sessionId
        }

    val onSmartRefreshCommand = createTypeCommand<SmartRefreshLayout> {
        messageList.value?.let {
            loadMessage(it[0].message, this)
        }
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
        refreshLayout: SmartRefreshLayout? = null,
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
                    val data = list.plus(messageList.value ?: listOf())
                    messageList.value = data
                    if (true == isFirst) {
                        onScrollPositionsEvent.value = data.size - 1
                    } else {
                        onScrollPositionsEvent.value=list.size
                    }
                    refreshLayout?.finishRefresh()
                },
                {
                    handleThrowable(it)
                }
            )
    }


}