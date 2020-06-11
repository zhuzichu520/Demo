package com.netease.nim.demo.ui.user.domain

import com.google.common.base.Optional
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/11 4:09 PM
 * since: v 1.0.0
 */
class UseCaseSetMessageNotify @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<UseCaseSetMessageNotify.Parameters, Flowable<Optional<Void>>>() {

    override fun execute(parameters: Parameters): Flowable<Optional<Void>> {
        return nimRepository.setMessageNotify(parameters.account, parameters.isNotify)
            .bindToSchedulers()
            .bindToException()
    }

    data class Parameters(
        val account: String,
        val isNotify: Boolean
    )

}