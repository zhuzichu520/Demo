package com.netease.nim.demo.ui.avchat.fragment

import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentAvchatBinding
import com.netease.nim.demo.ui.avchat.arg.ArgAvchat
import com.netease.nim.demo.ui.avchat.viewmodel.ViewModelAvchat

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/29 11:33 AM
 * since: v 1.0.0
 */
class FragmentAvchat : FragmentBase<FragmentAvchatBinding, ViewModelAvchat, ArgAvchat>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_avchat

}