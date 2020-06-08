package com.netease.nim.demo.ui.user.domain

import com.google.common.base.Optional
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.RecentContact
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 获取最近会话列表
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
class UseCaseGetSession @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<String, Flowable<Optional<RecentContact>>>() {

    override fun execute(parameters: String): Flowable<Optional<RecentContact>> {

        return nimRepository.getRecentContact(parameters, SessionTypeEnum.P2P).bindToSchedulers()
            .bindToException()

    }
}