package com.netease.nim.demo.nim

import com.google.common.base.Optional
import com.hiwitech.android.shared.ext.logi
import com.hiwitech.android.shared.http.exception.NimError
import com.hiwitech.android.shared.http.exception.NimThrowable
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.avchat.AVChatCallback
import io.reactivex.FlowableEmitter

/**
 * desc IM请求封装
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class NimAvChatCallback<T>(
    private val emitter: FlowableEmitter<Optional<T>>
) : AVChatCallback<T> {

    override fun onSuccess(any: T?) {
        emitter.onNext(Optional.fromNullable(any))
        emitter.onComplete()
    }

    override fun onFailed(code: Int) {
        "AvChat出现了错误:".plus(code).logi()
        emitter.onError(
            NimThrowable(
                code,
                NimError.toMessage(code)
            )
        )
        emitter.onComplete()
    }

    override fun onException(throwable: Throwable) {
        emitter.onError(throwable)
        emitter.onComplete()
    }

}