package com.hiwitech.android.shared.http.exception

class NimThrowable(val code: Int, override val message: String) : RuntimeException()