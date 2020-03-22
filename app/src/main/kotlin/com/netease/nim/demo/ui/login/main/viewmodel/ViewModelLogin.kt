package com.netease.nim.demo.ui.login.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.nim.NimRequestCallback
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.login.main.domain.UseCaseLogin
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.LoginInfo
import com.uber.autodispose.autoDispose
import com.zhuzichu.android.libs.internal.MainHandler
import com.zhuzichu.android.libs.tool.md5
import com.zhuzichu.android.mvvm.base.ArgDefault
import com.zhuzichu.android.shared.ext.autoLoading
import com.zhuzichu.android.shared.ext.createCommand
import com.zhuzichu.android.shared.ext.toast
import javax.inject.Inject

class ViewModelLogin @Inject constructor(
    private val useCaseLogin: UseCaseLogin
) : ViewModelBase<ArgDefault>() {

    val account = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val onClickLogin = createCommand {
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