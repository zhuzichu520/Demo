package com.netease.nim.demo.ui.file.arg

import com.hiwitech.android.mvvm.base.BaseArg
import kotlinx.android.parcel.Parcelize

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/26 11:17 AM
 * since: v 1.0.0
 */
@Parcelize
class ArgFile(
    val type: Int,
    val max: Int = 9
) : BaseArg() {
    companion object {
        //消息页面跳转过来的
        const val TYPE_MESSAGE = 1
        //web界面跳转过来的
        const val TYPE_WEB = 2
    }
}