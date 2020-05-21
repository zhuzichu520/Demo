package com.netease.nim.demo.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.netease.nim.demo.R

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/21 5:10 PM
 * since: v 1.0.0
 */
class ViewEmpty @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_empty,this)
    }
}