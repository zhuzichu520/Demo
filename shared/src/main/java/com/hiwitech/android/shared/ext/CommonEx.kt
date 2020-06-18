package com.hiwitech.android.shared.ext

import android.content.ContentResolver
import android.net.Uri
import android.text.Editable
import com.hiwitech.android.shared.global.AppGlobal.context

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/16 11:20 AM
 * since: v 1.0.0
 */
fun String?.toEditable(): Editable {
    return Editable.Factory.getInstance().newEditable(this)
}

fun Int.toUri(): Uri {
    return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + this)
}