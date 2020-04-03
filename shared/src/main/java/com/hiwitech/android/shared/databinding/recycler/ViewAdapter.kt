package com.hiwitech.android.shared.databinding.recycler

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.hiwitech.android.mvvm.databinding.BindingCommand
import com.hiwitech.android.shared.rxbinding.scrollBottom
import com.hiwitech.android.shared.widget.recycler.LineManager
import com.hiwitech.android.widget.recycler.BottomRecyclerView
import java.util.concurrent.TimeUnit

@BindingAdapter(value = ["onBottomCommand"], requireAll = false)
fun bindRecyclerView(
    recyclerView: BottomRecyclerView,
    onBottomCommand: BindingCommand<*>?
) {
    onBottomCommand?.apply {
        recyclerView.scrollBottom()
            .throttleFirst(100, TimeUnit.MILLISECONDS)
            .subscribe {
                execute()
            }
    }
}

@BindingAdapter(value = ["onRefreshCommand"], requireAll = false)
fun bindSwipeRefreshLayout(
    swipeRefreshLayout: SwipeRefreshLayout,
    onRefreshCommand: BindingCommand<SwipeRefreshLayout>?
) {
    swipeRefreshLayout.setOnRefreshListener {
        onRefreshCommand?.execute(swipeRefreshLayout)
    }
}


@BindingAdapter("lineManager")
fun setLineManager(
    recyclerView: RecyclerView,
    factory: LineManager.Factory?
) {
    factory?.let {
        recyclerView.addItemDecoration(it.create(recyclerView))
    }
}

@BindingAdapter(value = ["onSmartRefreshCommand", "onSmartLoadMoreCommand"], requireAll = false)
fun bindSmartRefreshLayout(
    smartRefreshLayout: SmartRefreshLayout,
    onSmartRefreshCommand: BindingCommand<SmartRefreshLayout>?,
    onSmartLoadMoreCommand: BindingCommand<SmartRefreshLayout>?
) {

    onSmartRefreshCommand?.apply {
        smartRefreshLayout.setOnRefreshListener {
            execute(it)
        }
    }

    onSmartLoadMoreCommand?.apply {
        smartRefreshLayout.setOnLoadMoreListener {
            execute(it)
        }
    }

}