package com.netease.nim.demo.ui.message.main.fragment

import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentMeBinding
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.netease.nim.demo.ui.message.main.viewmodel.ViewModelMessage
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum

class FragmentMessage : FragmentBase<FragmentMeBinding, ViewModelMessage, ArgMessage>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_message


    override fun initLazyData() {
        super.initLazyData()
        val sessionType = SessionTypeEnum.typeOfValue(arg.sessionType)
        loadData(sessionType)
    }

    private fun loadData(sessionType: SessionTypeEnum) {
        when (sessionType) {
            SessionTypeEnum.P2P -> {
                viewModel.loadUserInfo()
            }
            SessionTypeEnum.Team -> {
                viewModel.loadTeamInfo()
            }
        }
    }

}