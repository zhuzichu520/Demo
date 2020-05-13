package com.hiwitech.android.shared.tools

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/12 10:24 AM
 * since: v 1.0.0
 */
class Weak<T : Any>(initializer: () -> T?) {

    private var weakReference = WeakReference(initializer())

    constructor() : this({
        null
    })

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return weakReference.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        weakReference = WeakReference(value)
    }

}