package com.netease.nim.demo.ui.login.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.LoginInfo
import com.zhuzichu.android.libs.tool.EncryptType
import com.zhuzichu.android.libs.tool.getEncryptString
import com.zhuzichu.android.mvvm.base.ArgDefault
import com.zhuzichu.android.shared.ext.createCommand
import com.zhuzichu.android.shared.ext.toast
import javax.inject.Inject

class ViewModelLogin @Inject constructor() : ViewModelBase<ArgDefault>() {

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

        showLoading()
        NIMClient.getService(AuthService::class.java)
            .login(LoginInfo(account, getEncryptString(password, EncryptType.ENC_TYPE_MD5)))
            .setCallback(object : RequestCallback<LoginInfo> {
                override fun onSuccess(loginInfo: LoginInfo) {
                    "登录成功".toast()
                    hideLoading()
                }

                override fun onFailed(code: Int) {
                    "onFailed".toast()
                    hideLoading()
                }

                override fun onException(throwable: Throwable) {
                    "onException".toast()
                    hideLoading()
                }
            })
    }

}