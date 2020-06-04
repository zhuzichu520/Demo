package com.netease.nim.demo.ui.multiport.fragment

import androidx.lifecycle.Observer
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentMultiportBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.multiport.arg.ArgMultiport
import com.netease.nim.demo.ui.multiport.viewmodel.ViewModelMultiport

/**
 * desc 注册Fragment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentMultiport :
    FragmentBase<FragmentMultiportBinding, ViewModelMultiport, ArgMultiport>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_multiport

    override fun initOneData() {
        super.initOneData()
        viewModel.updateOnlienClent(arg.list)
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.toObservable(NimEvent.OnLienClientEvent::class.java, Observer {
            val list = it.list ?: listOf()
            viewModel.updateOnlienClent(list)
        })
    }

}