package com.netease.nim.demo.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.hiwitech.android.libs.tool.getStatusBarHeight
import com.hiwitech.android.libs.tool.toCast
import com.hiwitech.android.mvvm.base.BaseArg
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.toast
import com.hiwitech.android.shared.global.AppGlobal.context
import com.hiwitech.android.shared.http.exception.BusinessThrowable
import com.hiwitech.android.shared.widget.decoration.SuperOffsetDecoration
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * desc viewModel基类
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
abstract class ViewModelBase<TArg : BaseArg> : BaseViewModel<TArg>(), IBus {

    override val observers: HashMap<Class<Any>, Observer<Any>> get() = hashMapOf()

    val statusBarHeight = MutableLiveData(getStatusBarHeight(context = context))

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
        //清理当前所有事件
        removeObserables()
    }

    fun dividerNone(): Int = SuperOffsetDecoration.SHOW_DIVIDER_NONE
    fun dividerBeginning(): Int = SuperOffsetDecoration.SHOW_DIVIDER_BEGINNING
    fun dividerMiddle(): Int = SuperOffsetDecoration.SHOW_DIVIDER_MIDDLE
    fun dividerEnd(): Int = SuperOffsetDecoration.SHOW_DIVIDER_END
}