package com.netease.nim.demo.ui.message.databinding

import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.netease.nim.demo.nim.emoji.ToolMoon

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/10 6:25 PM
 * since: v 1.0.0
 */

@BindingAdapter("moonText")
fun bindMoonTextView(
    textView: TextView,
    text: String?
) {
    text?.let {
        ToolMoon.identifyFaceExpression(
            textView.context,
            textView,
            it,
            ImageSpan.ALIGN_BOTTOM
        )
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
}