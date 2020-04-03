package com.hiwitech.android.shared.http.exception

class BusinessThrowable(var code: Int, override var message: String) : RuntimeException()
