package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.libs.tool.toCast
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.map
import com.hiwitech.android.shared.widget.page.PageHelper
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.nim.attachment.StickerAttachment
import com.netease.nim.demo.nim.audio.NimAudioManager
import com.netease.nim.demo.nim.tools.ToolImage
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.netease.nim.demo.ui.message.main.domain.*
import com.netease.nim.demo.ui.photobrowser.domain.UseCaseGetImageAndVideoMessage
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment
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
    private val useCaseGetMessageList: UseCaseGetMessageList,
    private val useCaseSendMessage: UseCaseSendMessage,
    private val useCaseDowloadAttachment: UseCaseDowloadAttachment,
    private val useCaseGetImageAndVideoMessage: UseCaseGetImageAndVideoMessage,
    private val nimAudioManager: NimAudioManager
) : ViewModelBase<ArgMessage>() {

    private val pageSize = 30

    /**
     * 滑动事件
     */
    val onScrollBottomEvent = SingleLiveEvent<Unit>()

    /**
     * 添加数据完成事件
     */
    val onAddMessageCompletedEvent = SingleLiveEvent<List<Any>>()

    /**
     * 标题
     */
    val title = MutableLiveData<String>()


    /**
     * 是否显示录音布局
     */
    val isShownRecord = MutableLiveData<Boolean>(false)

    /**
     * 录音时间显示
     */
    val recordTime = MutableLiveData<String>()

    /**
     * 录音界面的文案提示
     */
    val timerTip = MutableLiveData<Int>()

    /**
     * 录音界面的背景色
     */
    val timerTipBackgroundColor = MutableLiveData<Int>()

    /**
     * 消息数据
     */
    private val messageList = ObservableArrayList<Any>()

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
        map<ItemViewModelImageGifMessage>(BR.item, R.layout.item_message_image_gif)
        map<ItemViewModelImageMessage>(BR.item, R.layout.item_message_image)
        map<ItemViewModelVideoMessage>(BR.item, R.layout.item_message_video)
        map<ItemViewModelStickerMessage>(BR.item, R.layout.item_message_sticker)
        map<ItemViewModelAudioMessage>(BR.item, R.layout.item_message_audio)
        map<ItemViewModelUnknownMessage>(BR.item, R.layout.item_message_unknown)
        map<ItemViewModelLocationMessage>(BR.item, R.layout.item_message_location)
    }

    /**
     * 共享元素消息的uuid
     */
    val shareElementUuid = MutableLiveData<String>()

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
        addMessage(listOf(message), true)
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
                    pageHelper.add(list, true)
                    if (true == isFirst) {
                        onScrollBottomEvent.call()
                    }
                },
                {
                    it.printStackTrace()
                    handleThrowable(it)
                }
            )
    }

    /**
     * 添加新消息
     *
     * @param list 消息集合
     * @param isEvent 是否开启添加完成事件
     */
    fun addMessage(list: List<IMMessage>, isEvent: Boolean) {
        val data = handleMessageList(list)
        data.forEach { item ->
            val index = messageList.indexOf(item)
            if (index != -1) {
                //已经存在
                messageList[index] = item
            } else {
                //不存在
                messageList.add(item)
            }
        }
        if (isEvent) {
            onAddMessageCompletedEvent.value = messageList
        }
    }

    /**
     * 处理消息集合返回数据模型集合
     * @param list 消息集合
     */
    private fun handleMessageList(list: List<IMMessage>): List<ItemViewModelBaseMessage> {
        return list.map { item ->
            when (item.msgType) {
                MsgTypeEnum.text -> {
                    ItemViewModelTextMessage(this, item)
                }
                MsgTypeEnum.image -> {
                    convertItemViewModelImageMessage(item)
                }
                MsgTypeEnum.audio -> {
                    ItemViewModelAudioMessage(this, item, useCaseDowloadAttachment, nimAudioManager)
                }
                MsgTypeEnum.location -> {
                    ItemViewModelLocationMessage(this, item)
                }
                MsgTypeEnum.video -> {
                    ItemViewModelVideoMessage(
                        this,
                        item,
                        useCaseDowloadAttachment,
                        useCaseGetImageAndVideoMessage
                    )
                }
                MsgTypeEnum.custom -> {
                    val attachment = item.attachment
                    if (attachment is StickerAttachment) {
                        ItemViewModelStickerMessage(this, item)
                    } else {
                        ItemViewModelUnknownMessage(this, item)
                    }
                }
                else -> {
                    ItemViewModelUnknownMessage(this, item)
                }
            }
        }
    }

    /**
     * 将IMesasge转换成ItemViewModelImageMessage
     */
    private fun convertItemViewModelImageMessage(item: IMMessage): ItemViewModelImageMessage {
        return if (ToolImage.isGif((item.attachment as ImageAttachment).extension)) {
            ItemViewModelImageGifMessage(
                this,
                item,
                useCaseDowloadAttachment,
                useCaseGetImageAndVideoMessage
            )
        } else {
            ItemViewModelImageMessage(
                this,
                item,
                useCaseDowloadAttachment,
                useCaseGetImageAndVideoMessage
            )
        }
    }

    /**
     * 通过Message获取下标位置
     */
    fun getIndexByUuid(uuid: String): Int? {
        messageList.forEachIndexed { index, item ->
            if ((item as ItemViewModelBaseMessage).uuid == uuid) {
                return index
            }
        }
        return null
    }

    /**
     * 通过下标获取ItemViewModel
     */
    fun getItemViewModelByIndex(index: Int): ItemViewModelBaseMessage? {
        if (index < 0) {
            return null
        }
        if (index >= messageList.size) {
            return null
        }
        return messageList[index].toCast()
    }

}