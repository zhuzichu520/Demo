package com.netease.nim.demo.ui.contact.fragment

import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentContactBinding
import com.netease.nim.demo.ui.contact.viewmodel.ViewModelContact
import com.hiwitech.android.mvvm.base.ArgDefault

class FragmentContact : FragmentBase<FragmentContactBinding, ViewModelContact, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_contact

}