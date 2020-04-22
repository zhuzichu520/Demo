package com.netease.nim.demo.ui.message.emoticon.fragment

import androidx.lifecycle.Observer
import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentEmoticonBinding
import com.netease.nim.demo.ui.message.emoticon.viewmodel.ViewModelEmoticon

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/13 2:45 PM
 * since: v 1.0.0
 */
class FragmentEmoticon : FragmentBase<FragmentEmoticonBinding, ViewModelEmoticon, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_emoticon

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.tabIndex.observe(viewLifecycleOwner, Observer {
            viewModel.updateTabs()
        })
    }

    override fun initData() {
        super.initData()
        viewModel.tabIndex.value = 0
    }

}