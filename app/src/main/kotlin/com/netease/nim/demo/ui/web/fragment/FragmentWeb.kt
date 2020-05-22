package com.netease.nim.demo.ui.web.fragment

import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentWebBinding
import com.netease.nim.demo.ui.web.viewmodel.ViewModelWeb

/**
 * desc 个人中心fragment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentWeb : FragmentBase<FragmentWebBinding, ViewModelWeb, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_web

}