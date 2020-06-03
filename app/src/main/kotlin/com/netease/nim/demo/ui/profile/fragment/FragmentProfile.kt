package com.netease.nim.demo.ui.profile.fragment

import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentProfileBinding
import com.netease.nim.demo.ui.main.ActivityMain
import com.netease.nim.demo.ui.profile.viewmodel.ViewModelProfile
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 11:39 AM
 * since: v 1.0.0
 */
class FragmentProfile : FragmentBase<FragmentProfileBinding, ViewModelProfile, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_profile

    override fun initOneData() {
        super.initOneData()
        viewModel.loadUserInfo()
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.onLogOutEvent.observe(viewLifecycleOwner, Observer {
            showSnackbar()
        })
    }

    private fun showSnackbar() {
        Snackbar.make(root, R.string.logout_info, 1000)
            .setAction(R.string.confirm) {
                ActivityMain.logout(requireActivity())
            }.show()
    }

}