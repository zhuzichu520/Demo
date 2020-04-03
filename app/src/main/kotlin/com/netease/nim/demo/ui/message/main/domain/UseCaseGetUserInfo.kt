package com.netease.nim.demo.ui.message.main.domain

import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import io.reactivex.Flowable
import javax.inject.Inject

class UseCaseGetUserInfo @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<String, Flowable<NimUserInfo>>() {

    override fun execute(parameters: String): Flowable<NimUserInfo> {
        return nimRepository.getUserInfoById(parameters).bindToSchedulers().bindToException()
    }
}