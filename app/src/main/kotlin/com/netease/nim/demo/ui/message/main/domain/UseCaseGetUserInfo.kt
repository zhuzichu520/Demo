package com.netease.nim.demo.ui.message.main.domain

import com.google.common.base.Optional
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 获取IM用户资料详情
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseGetUserInfo @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<String, Flowable<Optional<NimUserInfo>>>() {

    override fun execute(parameters: String): Flowable<Optional<NimUserInfo>> {
        return nimRepository.getUserInfoById(parameters).bindToSchedulers().bindToException()
    }
}