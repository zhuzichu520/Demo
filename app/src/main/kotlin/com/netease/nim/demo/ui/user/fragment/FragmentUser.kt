package com.netease.nim.demo.ui.user.fragment

import com.google.android.material.snackbar.Snackbar
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentUserBinding
import com.netease.nim.demo.ui.user.arg.ArgUser
import com.netease.nim.demo.ui.user.viewmodel.ViewModelUser

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/3 2:29 PM
 * since: v 1.0.0
 */
class FragmentUser : FragmentBase<FragmentUserBinding, ViewModelUser, ArgUser>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_user

    override fun initOneData() {
        super.initOneData()
        viewModel.loadUserInfo()
    }

}