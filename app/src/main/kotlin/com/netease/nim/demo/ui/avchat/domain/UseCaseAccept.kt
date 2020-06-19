package com.netease.nim.demo.ui.avchat.domain

import com.google.common.base.Optional
import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.hiwitech.android.shared.global.AppGlobal.context
import com.netease.nim.demo.nim.config.AVChatConfigs
import com.netease.nim.demo.nim.config.AVPrivatizationConfig
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nimlib.sdk.avchat.AVChatManager
import com.netease.nimlib.sdk.avchat.model.AVChatParameters
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 挂断电话
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseAccept @Inject constructor(
    private val nimRepository: NimRepository,
    private val chatManager: AVChatManager,
    private val avChatConfigs: AVChatConfigs
) : UseCase<Long, Flowable<Optional<Void>>>() {

    override fun execute(parameters: Long): Flowable<Optional<Void>> {

        chatManager.enableRtc(AVPrivatizationConfig.getServerAddresses(context))
        chatManager.setParameters(avChatConfigs.avChatParameters)
        chatManager.setParameter(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true)

        return nimRepository.accept(
            parameters
        ).bindToSchedulers().bindToException()

    }

}