package com.netease.nim.demo.ui.multiport.domain

import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.auth.OnlineClient
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 获取IM用户资料详情
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseKickOtherOut @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<OnlineClient, Flowable<Void>>() {

    override fun execute(parameters: OnlineClient): Flowable<Void> {
        return nimRepository.kickOtherOut(
            parameters
        ).bindToSchedulers().bindToException()
    }


}