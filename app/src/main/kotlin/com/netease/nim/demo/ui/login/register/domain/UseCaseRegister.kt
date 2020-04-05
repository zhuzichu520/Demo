package com.netease.nim.demo.ui.login.register.domain

import com.google.common.base.Optional
import com.hiwitech.android.libs.tool.md5
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.repository.RemoteRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * desc 注册UseCase
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseRegister @Inject constructor(
    private val remoteRepository: RemoteRepository
) : UseCase<UseCaseRegister.Parameters, Observable<Optional<String>>>() {

    override fun execute(parameters: Parameters): Observable<Optional<String>> =
        remoteRepository.register(
            parameters.account,
            md5(parameters.password),
            parameters.nickname
        ).bindToSchedulers().bindToException()


    data class Parameters(
        val account: String,
        val password: String,
        val nickname: String
    )
}