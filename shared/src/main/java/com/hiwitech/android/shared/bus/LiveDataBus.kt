package com.hiwitech.android.shared.bus

import androidx.lifecycle.Observer
import com.hiwitech.android.shared.log.lumberjack.T
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jeremyliao.liveeventbus.core.Observable

/**
 * Created by Android Studio.
 * Blog: zhuzichu.com
 * User: zhuzichu
 * Date: 2020-02-17
 * Time: 16:39
 */
object LiveDataBus {

    fun post(event: Any) {
        LiveEventBus.get(event::class.java.simpleName).post(event)
    }

    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return LiveEventBus.get(eventType.simpleName, eventType)
    }

    fun <T> removeObserver(eventType: Class<T>, observer: Observer<T>) {
        LiveEventBus.get(eventType.simpleName, eventType).removeObserver(observer)
    }

}