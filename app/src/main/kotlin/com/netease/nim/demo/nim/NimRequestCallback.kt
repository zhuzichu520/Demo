package com.netease.nim.demo.nim

import com.netease.nimlib.sdk.RequestCallback
import com.hiwitech.android.shared.http.exception.NimError
import com.hiwitech.android.shared.http.exception.NimThrowable
import io.reactivex.FlowableEmitter

class NimRequestCallback<T>(
    private val emitter: FlowableEmitter<T>
) : RequestCallback<T> {

    override fun onSuccess(any: T) {
        emitter.onNext(any)
        emitter.onComplete()
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