package com.netease.nim.demo.ui.user.domain

import com.google.common.base.Optional
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.friend.constant.FriendFieldEnum
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/11 9:47 PM
 * since: v 1.0.0
 */
class UseCaseUpdateFriendFields @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<UseCaseUpdateFriendFields.Parameters, Flowable<Optional<Void>>>() {

    override fun execute(parameters: Parameters): Flowable<Optional<Void>> {

        return nimRepository.updateFriendFields(
            parameters.account,
            parameters.fields
        ).bindToException()

    }

    data class Parameters(
        val account: String,
        val fields: Map<FriendFieldEnum, Any>
    )
}