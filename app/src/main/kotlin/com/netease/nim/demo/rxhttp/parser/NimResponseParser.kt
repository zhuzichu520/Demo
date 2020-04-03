package com.netease.nim.demo.rxhttp.parser

import com.google.common.base.Optional
import com.hiwitech.android.shared.http.entity.NimReponse
import com.hiwitech.android.shared.http.exception.BusinessThrowable
import com.hiwitech.android.shared.http.exception.ExceptionManager
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.entity.ParameterizedTypeImpl
import rxhttp.wrapper.parse.AbstractParser
import java.lang.reflect.Type

@Parser(name = "NimResponse")
class NimResponseParser<T>(type: Type) : AbstractParser<Optional<T>>(type) {
    override fun onParse(response: okhttp3.Response): Optional<T> {
        val type: Type = ParameterizedTypeImpl.get(NimReponse::class.java, mType) //获取泛型类型
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