package com.zhuzichu.android.shared.ext

import com.zhuzichu.android.mvvm.databinding.BindingCommand

fun createCommand(closure: () -> Unit): BindingCommand<Any> {
    return BindingCommand({
        closure.invoke()
    })
}

fun <T> createTypeCommand(closure: T?.() -> Unit): BindingCommand<T?> {
    return BindingCommand(consumer = {
        closure.invoke(this)
    })
}

fun Any.className(): String {
    return this.javaClass.simpleName
}

inline fun <reified T: Any> new(): T {
    val clz = T::class.java
    val mCreate = clz.getDeclaredConstructor()
    mCreate. isAccessible = true
    return mCreate. newInstance()
}