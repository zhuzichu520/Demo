package com.netease.nim.demo.nim

import com.netease.nimlib.sdk.RequestCallback

abstract class NimRequestCallback<T> : RequestCallback<T> {

    override fun onSuccess(any: T) {

    }

    override fun onFailed(code: Int) {

    }

    override fun onException(throwable: Throwable) {
    }

}