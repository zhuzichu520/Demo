package com.netease.nim.demo.ui.login.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.libs.tool.md5
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.ext.autoLoading
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.toast
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.login.main.domain.UseCaseLogin
import com.netease.nimlib.sdk.auth.LoginInfo
import com.uber.autodispose.autoDispose
import javax.inject.Inject

/**
 * desc 登录ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelLogin @Inject constructor(
    private val useCaseLogin: UseCaseLogin
) : ViewModelBase<ArgDefault>() {

    /**
     * 账号
     */
    val account = MutableLiveData<String>("a7711451")
    /**
     * 密码
     */
    val password = MutableLiveData<String>("7711451")

    /**
     * 点击登录
     */
    val onClickLoginCommand = createCommand {
        val account = account.value
        val password = password.value
        if (account.isNullOrEmpty()) {
            "账号不能为空".toast()
            return@createCommand
        }
        if (password.isNullOrEmpty()) {
            "密码不能为空".toast()
            return@createCommand
        }
        useCaseLogin.execute(LoginInfo(account, md5(password)))
            .autoLoading(this)
            .autoDispose(this)
            .subscribe(
                {
                    NimUserStorage.login(it)
                    showMainPage()
                },
                {
                    handleThrowable(it)
                }
            )
    }

    /**
     * 点击注册
     */
    val onClickRegisterCommand = createCommand {
        start(R.id.action_fragmentLogin_to_fragmentRegister)
    }

    /**
     * 跳转MainFragment
     */
    private fun showMainPage() {
        MainHandler.postDelayed {
            start(
                R.id.action_fragmentLogin_to_navigation_main,
                popUpTo = R.id.fragmentLogin,
                inclusive = true
            )
        }
    }
}