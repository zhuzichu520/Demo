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
import com.netease.nimlib.sdk.avchat.constant.AVChatType
import com.netease.nimlib.sdk.avchat.model.AVChatData
import com.netease.nimlib.sdk.avchat.model.AVChatNotifyOption
import com.netease.nimlib.sdk.avchat.model.AVChatParameters
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 挂断电话
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseHangUp @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<Long, Flowable<Optional<Void>>>() {

    override fun execute(parameters: Long): Flowable<Optional<Void>> {

        return nimRepository.hangUp(
            parameters
        ).bindToSchedulers().bindToException()
    }

}