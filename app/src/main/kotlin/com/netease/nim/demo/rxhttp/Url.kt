package com.netease.nim.demo.rxhttp

import com.netease.nim.demo.BuildConfig
import rxhttp.wrapper.annotation.DefaultDomain

object Url {
    @DefaultDomain()
    const val baseUrl = BuildConfig.HOST_APP2
}