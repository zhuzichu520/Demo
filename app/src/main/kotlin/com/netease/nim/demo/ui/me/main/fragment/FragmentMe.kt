package com.netease.nim.demo.ui.me.main.fragment

import androidx.lifecycle.Observer
import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentMeBinding
import com.netease.nim.demo.ui.me.main.viewmodel.ViewModelMe
import com.netease.nim.demo.ui.profile.event.EventProfile
import com.netease.nim.demo.ui.theme.fragment.FragmentTheme

/**
 * desc 个人中心fragment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentMe : FragmentBase<FragmentMeBinding, ViewModelMe, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_me

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.onClickThemeEvent.observe(viewLifecycleOwner, Observer {
            FragmentTheme().show(childFragmentManager)
        })

        viewModel.toObservable(EventProfile.OnUpdateUserInfoEvent::class.java, Observer {
            viewModel.updateUserInfo()
        })

    }

    override fun initOneData() {
        super.initOneData()
        viewModel.updateUserInfo()
    }


}