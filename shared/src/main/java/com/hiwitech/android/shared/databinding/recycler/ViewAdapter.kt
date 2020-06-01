
package com.hiwitech.android.shared.databinding.recycler

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hiwitech.android.libs.tool.dp2px
import com.hiwitech.android.mvvm.databinding.BindingCommand
import com.hiwitech.android.shared.R
import com.hiwitech.android.shared.ext.toColorByResId
import com.hiwitech.android.shared.widget.decoration.SuperOffsetDecoration
import com.hiwitech.android.widget.recycler.UpPullRecycylerViewOnScrollListener

@BindingAdapter(value = ["onLoadMoreCommand", "onRefreshCommand"], requireAll = false)
fun bindRecyclerViewScrollListener(
    recyclerView: RecyclerView,
    onLoadMoreCommand: BindingCommand<*>?,
    onRefreshCommand: BindingCommand<*>?
) {
    recyclerView.addOnScrollListener(UpPullRecycylerViewOnScrollListener(
        onLoadMoreData = {
            onLoadMoreCommand?.execute()
        },
        onRefreshData = {
            onRefreshCommand?.execute()
        }
    ))
}

@BindingAdapter(value = ["onSwipeRefreshCommand"], requireAll = false)
fun bindSwipeRefreshLayout(
    swipeRefreshLayout: SwipeRefreshLayout,
    onSwipeRefreshCommand: BindingCommand<SwipeRefreshLayout>?
) {
    swipeRefreshLayout.setOnRefreshListener {
        onSwipeRefreshCommand?.execute(swipeRefreshLayout)
    }
}


@BindingAdapter(value = ["linePaddingLeft", "linePaddingRight", "lineDividerColor", "lineShowDivider"], requireAll = false)
fun setLineManager(
    recyclerView: RecyclerView,
    linePaddingLeft: Float?,
    linePaddingRight: Float?,
    lineDividerColor: Int?,
    lineShowDivider: Int?
) {
    val context = recyclerView.context
    val paddingLeft = linePaddingLeft ?: 0F
    val paddingRight = linePaddingRight ?: 0F
    val dividerColor = lineDividerColor ?: R.color.color_divider.toColorByResId()
    val showDivider = lineShowDivider ?: SuperOffsetDecoration.SHOW_DIVIDER_MIDDLE

    val layoutManager = recyclerView.layoutManager
    if (layoutManager is LinearLayoutManager) {
        val decoration = SuperOffsetDecoration.Builder(layoutManager, recyclerView.context)
            .setPaddingLeft(dp2px(context, paddingLeft))
            .setPaddingRight(dp2px(context, paddingRight))
            .setDividerColor(dividerColor)
            .setShowDividers(showDivider)
            .setMainAxisSpace(dp2px(context, 0.75f))
            .build()
        recyclerView.addItemDecoration(decoration)
    }
}