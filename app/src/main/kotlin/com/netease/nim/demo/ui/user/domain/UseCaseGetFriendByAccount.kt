package com.netease.nim.demo.ui.user.domain

import com.google.common.base.Optional
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.friend.model.Friend
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 获取IM好友信息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseGetFriendByAccount @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<String, Flowable<Optional<Friend>>>() {

    override fun execute(parameters: String): Flowable<Optional<Friend>> {
        return nimRepository.getFriendByAccount(
            parameters
        ).bindToSchedulers().bindToException()
    }

}