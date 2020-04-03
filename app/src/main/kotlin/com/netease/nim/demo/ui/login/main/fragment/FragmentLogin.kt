package com.netease.nim.demo.ui.login.main.fragment

import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentLoginBinding
import com.netease.nim.demo.ui.login.main.viewmodel.ViewModelLogin
import com.hiwitech.android.mvvm.base.ArgDefault

class FragmentLogin : FragmentBase<FragmentLoginBinding, ViewModelLogin, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_login
    
}