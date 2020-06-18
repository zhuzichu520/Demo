package com.netease.nim.demo.ui.avchat.arg

import com.hiwitech.android.mvvm.base.BaseArg
import com.netease.nimlib.sdk.avchat.constant.AVChatType
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
    val type: Int,
    val account: String,
    val chatType: AVChatType,
    var data: AVChatData?
) : BaseArg(true) {

    companion object {

        //拨打电话
        const val TYPE_OUTGOING = 0

        //接听电话
        const val TYPE_INCOMING = 1

        fun fromAvCahtData(type: Int, data: AVChatData): ArgAvchat {
            return ArgAvchat(type, data.account, data.chatType, data)
        }

    }

}