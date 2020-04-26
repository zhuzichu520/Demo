package com.netease.nim.demo.ui.message.databinding

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.netease.nim.demo.nim.emoji.ToolMoon

/**
 * desc 表情Text
 * author: 朱子楚
 * time: 2020/4/10 6:25 PM
 * since: v 1.0.0
 */

@BindingAdapter(value = ["moonText"], requireAll = false)
fun bindMoonTextView(
    textView: TextView,
    moonText: String?
) {
    moonText?.let {
        ToolMoon.identifyFaceExpression(textView.context, textView, it)
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
}