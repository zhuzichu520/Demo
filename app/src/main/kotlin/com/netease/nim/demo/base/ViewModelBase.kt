package com.netease.nim.demo.base

import androidx.lifecycle.Observer
import com.hiwitech.android.libs.tool.toCast
import com.hiwitech.android.mvvm.base.BaseArg
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.toast
import com.hiwitech.android.shared.http.exception.BusinessThrowable
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * desc viewModel基类
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
abstract class ViewModelBase<TArg : BaseArg> : BaseViewModel<TArg>() {

    private val observers = hashMapOf<Class<Any>, Observer<Any>>()

    fun <T : Any> toObservable(eventType: Class<T>, observer: Observer<T>) {
        observers[eventType.toCast()] = observer.toCast()
        LiveEventBus.get(eventType.simpleName, eventType).observeForever(observer)
    }


    fun handleThrowable(throwable: Throwable) {
        throwable.printStackTrace()
        if (throwable is BusinessThrowable) {
            throwable.message.toast()
        } else {
            "未知错误".toast()
        }
    }

    override fun onCleared() {
        super.onCleared()
        observers.forEach {
            LiveEventBus.get(it.key.simpleName, it.key).removeObserver(it.value)
        }
    }
}