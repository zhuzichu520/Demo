package com.netease.nim.demo.base

import com.zhuzichu.android.mvvm.base.BaseArg
import com.zhuzichu.android.mvvm.base.BaseViewModel
import com.zhuzichu.android.shared.ext.toast
import com.zhuzichu.android.shared.http.exception.BusinessThrowable

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