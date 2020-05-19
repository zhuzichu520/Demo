package com.netease.nim.demo.ui.photobrowser.domain

import com.hiwitech.android.mvvm.domain.UseCase
import com.hiwitech.android.shared.ext.bindToException
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nim.demo.nim.tools.ToolImage
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment
import com.netease.nimlib.sdk.msg.model.IMMessage
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * desc 发送消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseGetImageAndVideoMessage @Inject constructor(
    private val nimRepository: NimRepository
) : UseCase<IMMessage, Flowable<List<IMMessage>>>() {

    override fun execute(parameters: IMMessage): Flowable<List<IMMessage>> {
        return nimRepository.getImageAndVideoMessage(
            parameters
        ).flatMap {
            Flowable.fromIterable(it)
        }.filter {
            (it.attachment as? ImageAttachment)?.let { attach ->
                !ToolImage.isGif(attach.extension)
            } ?: true
        }.toList().toFlowable().bindToSchedulers().bindToException()
    }


}