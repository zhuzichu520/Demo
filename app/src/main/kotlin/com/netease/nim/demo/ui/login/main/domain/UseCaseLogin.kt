package com.netease.nim.demo.ui.login.main.domain

import com.netease.nim.demo.nim.NimRequestCallback
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.LoginInfo
import com.zhuzichu.android.mvvm.domain.UseCase
import com.zhuzichu.android.shared.ext.bindToException
import com.zhuzichu.android.shared.ext.bindToSchedulers
import com.zhuzichu.android.shared.ext.createFlowable
import io.reactivex.Flowable
import javax.inject.Inject

class UseCaseLogin @Inject constructor(
    private val authService: AuthService
) : UseCase<LoginInfo, Flowable<LoginInfo>>() {

    override fun execute(parameters: LoginInfo): Flowable<LoginInfo> {

        return createFlowable<LoginInfo> {
            authService.login(parameters).setCallback(NimRequestCallback(this))
        }.bindToSchedulers().bindToException()

    }
}