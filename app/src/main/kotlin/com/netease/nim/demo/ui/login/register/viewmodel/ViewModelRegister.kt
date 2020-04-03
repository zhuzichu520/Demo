package com.netease.nim.demo.ui.login.register.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.login.register.domain.UseCaseRegister
import com.uber.autodispose.autoDispose
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.ext.autoLoading
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.toast
import javax.inject.Inject

class ViewModelRegister @Inject constructor(
    private val useCaseRegister: UseCaseRegister
) : ViewModelBase<ArgDefault>() {

    val account = MutableLiveData<String>("a7711451")
    val password = MutableLiveData<String>("7711451")
    val nickname = MutableLiveData<String>("7711451")

    val onClickRegisterCommand = createCommand {
        if (!checkRegisterContentValid())
            return@createCommand
        useCaseRegister.execute(
            UseCaseRegister.Parameters(
                account.value.toString(),
                password.value.toString(),
                nickname.value.toString()
            )
        ).autoLoading(this)
            .autoDispose(this)
            .subscribe(
                {
                  "注册成功".toast()
                },
                {
                    handleThrowable(it)
                }
            )
    }

    private fun checkRegisterContentValid(): Boolean {
        val account = account.value.toString()
        if (account.isEmpty() || account.length > 20) {
            R.string.register_account_tip.toast()
            return false
        }
        val nickname: String = nickname.value.toString()
        if (nickname.isEmpty() || nickname.length > 10) {
            R.string.register_nickname_tip.toast()
            return false
        }
        val password: String = password.value.toString()
        if (password.length < 6 || password.length > 20) {
            R.string.register_password_tip.toast()
            return false
        }
        return true
    }

}