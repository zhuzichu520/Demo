package com.netease.nim.demo.ui.photobrowser.arg

import com.hiwitech.android.mvvm.base.BaseArg
import com.netease.nimlib.sdk.msg.model.IMMessage
import kotlinx.android.parcel.Parcelize

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/12 3:04 PM
 * since: v 1.0.0
 */
@Parcelize
class ArgBrowserGif(
    val message: IMMessage
) : BaseArg(false)