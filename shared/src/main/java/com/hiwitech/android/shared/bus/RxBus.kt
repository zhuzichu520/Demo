package com.hiwitech.android.shared.bus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

object RxBus {

    private val mBus = PublishSubject.create<Any>().toSerialized()

    fun post(event: Any) {
        mBus.onNext(event)
    }

    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return mBus.ofType(eventType)
    }

    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

}