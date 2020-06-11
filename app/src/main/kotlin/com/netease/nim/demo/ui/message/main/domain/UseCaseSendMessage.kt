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
 * desc 发送消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseSendMessage @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<UseCaseSendMessage.Parameters, Flowable<Optional<Void>>>() {

    override fun execute(parameters: Parameters): Flowable<Optional<Void>> {
        return nimRepository.sendMessage(
            parameters.message,
            parameters.resend
        ).bindToSchedulers().bindToException()
    }

    data class Parameters(
        val message: IMMessage,
        val resend: Boolean
    )
}