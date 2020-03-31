package com.netease.nim.demo.repository

import com.google.common.base.Optional
import com.netease.nim.demo.BuildConfig
import io.reactivex.Observable
import rxhttp.wrapper.param.RxHttp

interface RemoteRepository {
    fun register(account: String, password: String, nickname: String): Observable<Optional<String>>
}

class RemoteRepositoryImpl : RemoteRepository {
    override fun register(
        account: String,
        password: String,
        nickname: String
    ): Observable<Optional<String>> {
        return RxHttp.postForm("/api/createDemoUser")
            .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
            .addHeader("User-Agent", "nim_demo_android")
            .addHeader("appkey", BuildConfig.NIM_APPKEY)
            .add("username", account)
            .add("password", password)
            .add("nickname", nickname)
            .asNimResponse(String::class.java)
    }

}