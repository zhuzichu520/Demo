package com.netease.nim.demo.ui.message.main.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentMeBinding
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.netease.nim.demo.ui.message.main.viewmodel.ViewModelMessage
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import kotlinx.android.synthetic.main.fragment_message.*
import javax.inject.Inject

class FragmentMessage : FragmentBase<FragmentMeBinding, ViewModelMessage, ArgMessage>() {

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
        viewModel.onScrollPositionsEvent.observe(viewLifecycleOwner, Observer {
            scrollToBottom(it)
        })
    }

    override fun onResume() {
        super.onResume()
        msgService.setChattingAccount(arg.contactId, sessionType)
    }


    override fun onPause() {
        super.onPause()
        msgService.setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None)
    }

    private fun scrollToBottom(position: Int) {
        if (position >= 0) {
            (recycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
        }
    }
}