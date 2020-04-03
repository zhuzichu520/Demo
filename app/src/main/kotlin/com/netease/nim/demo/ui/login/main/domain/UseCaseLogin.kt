package com.netease.nim.demo.ui.login.main.domain

import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.auth.LoginInfo
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import io.reactivex.Flowable
import javax.inject.Inject

class UseCaseLogin @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<LoginInfo, Flowable<LoginInfo>>() {

    override fun execute(parameters: LoginInfo): Flowable<LoginInfo> {
        return nimRepository.login(parameters).bindToSchedulers().bindToException()
    }

}