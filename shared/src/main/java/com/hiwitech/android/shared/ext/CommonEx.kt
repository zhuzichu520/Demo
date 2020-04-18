package com.hiwitech.android.shared.ext

import android.text.Editable

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/16 11:20 AM
 * since: v 1.0.0
 */
fun String?.toEditable(): Editable {
    return Editable.Factory.getInstance().newEditable(this)
}