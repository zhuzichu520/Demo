package com.netease.nim.demo.ui.login.main.fragment

import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.libs.tool.showKeyboard
import com.hiwitech.android.shared.ext.showSnackbar
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentLoginBinding
import com.netease.nim.demo.ui.login.main.arg.ArgLogin
import com.netease.nim.demo.ui.login.main.viewmodel.ViewModelLogin
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * desc 登录Fragment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentLogin : FragmentBase<FragmentLoginBinding, ViewModelLogin, ArgLogin>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_login

    override fun initView() {
        super.initView()

        account.post {
            account.setSelection(account.length())
        }

        if (arg.isKickOut) {
            root.showSnackbar(
                resId = R.string.kickout_info,
                duration = 4000,
                maxLines = 3,
                actionId = R.string.confirm
            )
        } else {
            MainHandler.postDelayed(200) {
                showKeyboard(requireContext(), account)
            }
        }

    }
}