package com.netease.nim.demo.ui.contact.fragment

import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentContactBinding
import com.netease.nim.demo.ui.contact.viewmodel.ViewModelContact

/**
 * desc 通讯录Fragment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentContact : FragmentBase<FragmentContactBinding, ViewModelContact, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_contact

}