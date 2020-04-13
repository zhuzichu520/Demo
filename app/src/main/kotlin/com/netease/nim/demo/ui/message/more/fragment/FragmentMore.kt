package com.netease.nim.demo.ui.message.more.fragment

import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.ext.toast
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentMoreBinding
import com.netease.nim.demo.ui.message.more.viewmodel.ViewModelMore

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/13 2:45 PM
 * since: v 1.0.0
 */
class FragmentMore : FragmentBase<FragmentMoreBinding, ViewModelMore, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_more

}