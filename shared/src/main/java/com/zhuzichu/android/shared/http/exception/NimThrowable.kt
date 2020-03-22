package com.zhuzichu.android.shared.http.exception

class NimThrowable(val code: Int, override val message: String) : RuntimeException()