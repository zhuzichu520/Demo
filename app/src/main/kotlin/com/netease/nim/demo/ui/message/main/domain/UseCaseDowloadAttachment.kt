package com.netease.nim.demo.ui.message.main.domain

import com.google.common.base.Optional
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.msg.model.IMMessage
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 获取IM用户资料详情
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseDowloadAttachment @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<UseCaseDowloadAttachment.Parameters, Flowable<Optional<Void>>>() {

    override fun execute(parameters: Parameters): Flowable<Optional<Void>> {
        return nimRepository.downloadAttachment(
            parameters.message,
            parameters.thumb
        ).bindToSchedulers().bindToException()
    }

    data class Parameters(
        val message: IMMessage,
        val thumb: Boolean
    )
}