package com.netease.nim.demo.rxhttp.parser

import com.google.common.base.Optional
import com.hiwitech.android.shared.http.entity.NimReponse
import com.hiwitech.android.shared.http.exception.BusinessThrowable
import com.hiwitech.android.shared.http.exception.ExceptionManager
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.entity.ParameterizedTypeImpl
import rxhttp.wrapper.parse.AbstractParser
import java.lang.reflect.Type

/**
 * desc IM 解析器
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Parser(name = "NimResponse", wrappers = [List::class])
open class NimResponseParser<T> : AbstractParser<Optional<T>> {

    constructor() : super()

    constructor(type: Type) : super(type)

    override fun onParse(response: okhttp3.Response): Optional<T> {
        val type: Type = ParameterizedTypeImpl[NimReponse::class.java, mType] //获取泛型类型
        val data: NimReponse<T> = convert(response, type)
        val t: T? = data.data
        if (data.res != 200) {
            throw BusinessThrowable(
                data.res ?: ExceptionManager.UNKNOWN,
                data.errmsg ?: "未知错误"
            )
        }
        return Optional.fromNullable(t)
    }
}