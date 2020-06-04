package com.netease.nim.demo.ui.login.main.fragment

import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
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
        if (arg.isKickOut) {
            val snacker = Snackbar.make(root, R.string.kickout_info, 4000)
            val textView = snacker.view.findViewById<TextView>(R.id.snackbar_text)
            textView.maxLines = 3
            snacker.setAction(R.string.confirm) {

            }.show()
        }
    }
}