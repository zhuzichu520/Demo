package com.netease.nim.demo.ui.message.main.arg

import com.zhuzichu.android.mvvm.base.BaseArg
import kotlinx.android.parcel.Parcelize

@Parcelize
class ArgMessage(
    val contactId: String,
    val sessionType: Int
) : BaseArg()