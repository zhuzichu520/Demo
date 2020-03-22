package com.netease.nim.demo.ui.session.fragment

import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentSessionBinding
import com.netease.nim.demo.ui.contact.viewmodel.ViewModelContact
import com.zhuzichu.android.mvvm.base.ArgDefault

class FragmentSession : FragmentBase<FragmentSessionBinding, ViewModelContact, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_contact

    override fun initView() {
        super.initView()

    }

}