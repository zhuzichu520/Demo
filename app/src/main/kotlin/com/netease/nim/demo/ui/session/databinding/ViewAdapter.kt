package com.netease.nim.demo.ui.session.databinding

import androidx.databinding.BindingAdapter
import com.netease.nim.demo.ui.session.view.ViewSessionBadge
import com.hiwitech.android.mvvm.databinding.BindingCommand
import com.hiwitech.android.widget.badge.Badge

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