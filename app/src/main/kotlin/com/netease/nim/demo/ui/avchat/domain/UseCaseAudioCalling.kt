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
 * desc 拨打语音电话
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseAudioCalling @Inject constructor(
    private val nimRepository: NimRepository,
    private val chatManager: AVChatManager,
    private val avChatConfigs: AVChatConfigs
) : UseCase<String, Flowable<Optional<AVChatData>>>() {

    override fun execute(parameters: String): Flowable<Optional<AVChatData>> {
        chatManager.enableRtc(AVPrivatizationConfig.getServerAddresses(context))
        chatManager.setParameters(avChatConfigs.avChatParameters)
        chatManager.setParameter(AVChatParameters.KEY_VIDEO_FRAME_FILTER, true)
        val notifyOption = AVChatNotifyOption()
        notifyOption.extendMessage = "extra_data"
        // 默认forceKeepCalling为true，开发者如果不需要离线持续呼叫功能可以将forceKeepCalling设为false
        // notifyOption.forceKeepCalling = false;
        return nimRepository.doCalling(
            parameters,
            AVChatType.AUDIO,
            notifyOption
        ).bindToSchedulers().bindToException()
    }

}