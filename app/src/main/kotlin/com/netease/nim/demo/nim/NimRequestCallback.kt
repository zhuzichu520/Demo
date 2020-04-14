package com.netease.nim.demo.nim

import com.hiwitech.android.shared.http.exception.NimError
import com.hiwitech.android.shared.http.exception.NimThrowable
import com.netease.nimlib.sdk.RequestCallback
import io.reactivex.FlowableEmitter

/**
 * desc IM请求封装
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class NimRequestCallback<T>(
    private val emitter: FlowableEmitter<T>
) : RequestCallback<T> {

    override fun onSuccess(any: T) {
        if (any == null) {
            emitter.onComplete()
        } else {
            emitter.onNext(any)
            emitter.onComplete()
        }
    }

    override fun onFailed(code: Int) {
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