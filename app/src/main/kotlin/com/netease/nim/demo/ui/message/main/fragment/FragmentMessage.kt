package com.netease.nim.demo.ui.message.main.fragment

import androidx.activity.addCallback
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.shared.bus.RxBus
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentMessageBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.netease.nim.demo.ui.message.main.viewmodel.ViewModelMessage
import com.netease.nim.demo.ui.message.view.ViewMessageInput
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.uber.autodispose.autoDispose
import kotlinx.android.synthetic.main.fragment_message.*
import javax.inject.Inject


/**
 * desc 消息界面Frqagment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentMessage : FragmentBase<FragmentMessageBinding, ViewModelMessage, ArgMessage>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_message

    private lateinit var sessionType: SessionTypeEnum

    @Inject
    lateinit var msgService: MsgService

    override fun initArgs(arg: ArgMessage) {
        super.initArgs(arg)
        sessionType = SessionTypeEnum.typeOfValue(arg.sessionType)
    }

    override fun initLazyData() {
        super.initLazyData()
        loadData(sessionType)
        viewModel.loadMessage(
            MessageBuilder.createEmptyMessage(arg.contactId, sessionType, 0),
            isFirst = true
        )
    }

    override fun initView() {
        super.initView()
        message_input.apply {
            onClickSendListener = {
                sendTextMessage(this)
            }
            recycler.post {
                attachContentView(layout_content, recycler)
                setInputType(ViewMessageInput.TYPE_DEFAULT)
            }
        }
        initBackListener()
    }

    private fun sendTextMessage(text: String) {
        val message = MessageBuilder.createTextMessage(arg.contactId, sessionType, text)
        viewModel.addMessage(listOf(message))
        viewModel.sendMessage(message)
    }

    /**
     * 返回监听
     */
    private fun initBackListener() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (ViewMessageInput.TYPE_EMOJI == message_input.getInputType() || ViewMessageInput.TYPE_MORE == message_input.getInputType()) {
                message_input.setInputType(ViewMessageInput.TYPE_DEFAULT)
            } else {
                navController.popBackStack()
            }
        }
    }

    /**
     * 加载相应的会话数据
     * @param sessionType 会话类型
     */
    private fun loadData(sessionType: SessionTypeEnum) {
        when (sessionType) {
            SessionTypeEnum.P2P -> {
                viewModel.loadUserInfo()
            }
            SessionTypeEnum.Team -> {
                viewModel.loadTeamInfo()
            }
            else -> {

            }
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        /**
         * recyclerview滑动到指定位置
         */
        viewModel.onScrollPositionsEvent.observe(viewLifecycleOwner, Observer {
            scrollToPosition(it)
        })

        /**
         * 添加数据完成
         */
        viewModel.onAddMessageCompletedEvent.observe(viewLifecycleOwner, Observer {
            if (isLastVisible()) {
                //最后一条可见 滑动到底部
                MainHandler.postDelayed { scrollToPosition(it.size - 1) }
            } else {
                //todo zhuzichu 最后一条不可见 显示提醒有新消息
            }
        })

        /**
         * 消息接收监听
         */
        RxBus.toObservable(NimEvent.OnReceiveMessageEvent::class.java)
            .bindToSchedulers()
            .autoDispose(viewModel)
            .subscribe {
                viewModel.addMessage(it.list)
            }

        /**
         * 消息发送状态监听
         */
        RxBus.toObservable(NimEvent.OnMessageStatusEvent::class.java)
            .bindToSchedulers()
            .autoDispose(viewModel)
            .subscribe {
                viewModel.addMessage(listOf(it.message))
            }
    }

    override fun onResume() {
        super.onResume()
        msgService.setChattingAccount(arg.contactId, sessionType)
    }


    override fun onPause() {
        super.onPause()
        msgService.setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None)
        message_input.setInputType(ViewMessageInput.TYPE_DEFAULT)
    }

    /**
     * 滑动到置顶位置
     */
    private fun scrollToPosition(position: Int) {
        if (position >= 0) {
            (recycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
        }
    }

    /**
     * 判断最后一条是否可见
     */
    private fun isLastVisible(): Boolean {
        val position = viewModel.items.size - 1
        val findLastPosition =
            (recycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        return findLastPosition == position
    }

}