package com.netease.nim.demo.ui.login.register.fragment

import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentRegisterBinding
import com.netease.nim.demo.ui.login.register.viewmodel.ViewModelRegister
import com.hiwitech.android.mvvm.base.ArgDefault

class FragmentRegister : FragmentBase<FragmentRegisterBinding, ViewModelRegister, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_register

}