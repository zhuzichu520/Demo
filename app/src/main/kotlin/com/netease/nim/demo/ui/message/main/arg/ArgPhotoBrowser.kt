package com.netease.nim.demo.ui.message.main.arg

import com.hiwitech.android.mvvm.base.BaseArg
import kotlinx.android.parcel.Parcelize

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/11 3:47 PM
 * since: v 1.0.0
 */
@Parcelize
class ArgPhotoBrowser(
    val urls: List<String>
) : BaseArg(true)