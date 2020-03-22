package com.netease.nim.demo.ui.session.fragment

import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentSessionBinding
import com.netease.nim.demo.ui.session.viewmodel.ViewModelSession
import com.zhuzichu.android.mvvm.base.ArgDefault

class FragmentSession : FragmentBase<FragmentSessionBinding, ViewModelSession, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_session

    override fun initLazyData() {
        super.initLazyData()
        viewModel.loadSessionList()
    }
}