package com.netease.nim.demo.ui.me.main.fragment

import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentMeBinding
import com.netease.nim.demo.ui.me.main.viewmodel.ViewModelMe
import com.hiwitech.android.mvvm.base.ArgDefault

class FragmentMe : FragmentBase<FragmentMeBinding, ViewModelMe, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_me

}