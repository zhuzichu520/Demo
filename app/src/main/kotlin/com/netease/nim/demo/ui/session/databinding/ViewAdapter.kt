package com.netease.nim.demo.ui.session.databinding

import androidx.databinding.BindingAdapter
import com.hiwitech.android.mvvm.databinding.BindingCommand
import com.hiwitech.android.widget.badge.Badge
import com.netease.nim.demo.ui.session.view.ViewSessionBadge

/**
 * desc 消息列表小红点业务Databinding
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
@BindingAdapter("number", "onDragStateChangedCommamnd")
fun bindViewSessionBadge(
    viewSessionBadge: ViewSessionBadge,
    number: Int?,
    onDragStateChangedCommamnd: BindingCommand<Int>?
) {
    number?.let {
        viewSessionBadge.number = it
    }

    onDragStateChangedCommamnd?.let {
        viewSessionBadge.onDragStateChangedListener =
            Badge.OnDragStateChangedListener { dragState, _, _ ->
                onDragStateChangedCommamnd.execute(dragState)
            }
    }
}