package com.netease.nim.demo.ui.multiport.arg

import com.hiwitech.android.mvvm.base.BaseArg
import com.netease.nimlib.sdk.auth.OnlineClient
import kotlinx.android.parcel.Parcelize

/**
 * desc 消息界面参数
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Parcelize
class ArgMultiport(
    val list: List<OnlineClient>
) : BaseArg()