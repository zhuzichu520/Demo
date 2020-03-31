package com.netease.nim.demo.ui.login.register.domain

import com.google.common.base.Optional
import com.netease.nim.demo.repository.RemoteRepository
import com.zhuzichu.android.libs.tool.md5
import com.zhuzichu.android.mvvm.domain.UseCase
import com.zhuzichu.android.shared.ext.bindToException
import com.zhuzichu.android.shared.ext.bindToSchedulers
import io.reactivex.Observable
import javax.inject.Inject

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