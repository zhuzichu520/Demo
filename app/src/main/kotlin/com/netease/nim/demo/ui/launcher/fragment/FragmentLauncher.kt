package com.netease.nim.demo.ui.launcher.fragment

import androidx.navigation.AnimBuilder
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentMainBinding
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.launcher.viewmodel.ViewModelLauncher

/**
 * desc 启动Fragment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentLauncher : FragmentBase<FragmentMainBinding, ViewModelLauncher, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_launcher

    override fun initView() {
        super.initView()
        MainHandler.postDelayed {
            if (!NimUserStorage.isLogin()) {
                start(R.id.action_fragmentLauncher_to_navigation_login)
            } else {
                start(R.id.action_fragmentLauncher_to_navigation_main)
            }
        }
    }

    private fun start(actionId: Int) {
        start(
            actionId,
            animBuilder = AnimBuilder().apply {
                enter = R.anim.no_anim
                exit = R.anim.no_anim
                popEnter = R.anim.no_anim
                popExit = R.anim.no_anim
            },
            popUpTo = R.id.fragmentLauncher,
            inclusive = true
        )
    }
}