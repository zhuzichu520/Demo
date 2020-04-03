package com.netease.nim.demo.base

import com.hiwitech.android.mvvm.base.BaseArg
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.toast
import com.hiwitech.android.shared.http.exception.BusinessThrowable

abstract class ViewModelBase<TArg : BaseArg> : BaseViewModel<TArg>() {

    fun handleThrowable(throwable: Throwable) {
        throwable.printStackTrace()
        if (throwable is BusinessThrowable) {
            throwable.message.toast()
        } else {
            "未知错误".toast()
        }
    }
}