package com.netease.nim.demo.base

import androidx.lifecycle.Observer
import com.hiwitech.android.libs.tool.toCast
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/17 10:38 AM
 * since: v 1.0.0
 */
interface IBus {

    val observers: HashMap<Class<Any>, Observer<Any>>

    /**
     * 注册事件 重复注册 新的事件会覆盖旧的事件
     */
    fun <T : Any> toObservable(eventType: Class<T>, observer: Observer<T>) {
        val key: Class<Any> = eventType.toCast()
        val value = observers[key]
        if (value != null) {
            LiveEventBus.get(eventType.simpleName, eventType).removeObserver(value.toCast())
        }
        LiveEventBus.get(eventType.simpleName, eventType).observeForever(observer)
        observers[key] = observer.toCast()
    }


    fun removeObserables() {
        observers.forEach {
            LiveEventBus.get(it.key.simpleName, it.key).removeObserver(it.value)
        }
    }

}