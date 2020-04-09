package com.netease.nim.demo.rxhttp.parser

import com.hiwitech.android.shared.http.entity.Response
import com.hiwitech.android.shared.http.entity.ResponsePageList
import com.hiwitech.android.shared.http.exception.BusinessThrowable
import com.hiwitech.android.shared.http.exception.ExceptionManager
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.entity.ParameterizedTypeImpl
import rxhttp.wrapper.parse.AbstractParser
import java.lang.reflect.Type

/**
 * desc 解析器
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Parser(name = "Response", wrappers = [List::class, ResponsePageList::class])
open class ResponseParser<T> : AbstractParser<T> {
    constructor() : super()
    constructor(type: Type) : super(type)

    override fun onParse(response: okhttp3.Response): T {
        val type: Type = ParameterizedTypeImpl[Response::class.java, mType] // 获取泛型类型
        val data: Response<T> = convert(response, type)
        val t: T? = data.data
        if (data.errorCode != 0 || t == null) {
            throw BusinessThrowable(
                data.errorCode ?: ExceptionManager.UNKNOWN,
                data.errorMsg ?: "未知错误"
            )
        }
        return t
    }
}