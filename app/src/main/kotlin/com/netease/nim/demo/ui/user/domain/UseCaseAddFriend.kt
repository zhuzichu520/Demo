package com.netease.nim.demo.ui.user.domain

import com.google.common.base.Optional
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.friend.constant.VerifyType
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 直接添加好友
 * author: 朱子楚
 * time: 2020/6/10 10:16 AM
 * since: v 1.0.0
 */
class UseCaseAddFriend @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<String, Flowable<Optional<Void>>>() {

    override fun execute(parameters: String): Flowable<Optional<Void>> {

        return nimRepository.addFriend(parameters, VerifyType.DIRECT_ADD, null)
            .bindToSchedulers()
            .bindToException()

    }
}