package com.netease.nim.demo.ui.avchat.arg

import com.hiwitech.android.mvvm.base.BaseArg
import com.netease.nim.demo.R
import com.netease.nimlib.sdk.avchat.model.AVChatData
import kotlinx.android.parcel.Parcelize

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/26 11:17 AM
 * since: v 1.0.0
 */
@Parcelize
class ArgAvchat(
    val data: AVChatData? = null
) : BaseArg(false, R.anim.no_anim, R.anim.no_anim, R.anim.no_anim, R.anim.no_anim)