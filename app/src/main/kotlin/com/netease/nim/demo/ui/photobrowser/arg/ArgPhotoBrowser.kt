package com.netease.nim.demo.ui.photobrowser.arg

import com.hiwitech.android.mvvm.base.BaseArg
import com.netease.nimlib.sdk.msg.model.IMMessage
import kotlinx.android.parcel.Parcelize

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/11 3:47 PM
 * since: v 1.0.0
 */
@Parcelize
class ArgPhotoBrowser(
    val message: IMMessage,
    val messageList: List<IMMessage>
) : BaseArg(true)