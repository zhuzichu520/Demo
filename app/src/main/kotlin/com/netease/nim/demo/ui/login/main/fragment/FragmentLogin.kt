package com.netease.nim.demo.ui.login.main.fragment

import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentLoginBinding
import com.netease.nim.demo.ui.login.main.viewmodel.ViewModelLogin

/**
 * desc 登录Fragment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentLogin : FragmentBase<FragmentLoginBinding, ViewModelLogin, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_login
    
}