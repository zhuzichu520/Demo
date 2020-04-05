package com.netease.nim.demo.ui.login.main.domain

import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.auth.LoginInfo
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 登录UseCase
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseLogin @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<LoginInfo, Flowable<LoginInfo>>() {

    override fun execute(parameters: LoginInfo): Flowable<LoginInfo> {
        return nimRepository.login(parameters).bindToSchedulers().bindToException()
    }

}