package com.zhuzichu.android.shared.databinding.view

import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEachIndexed
import androidx.databinding.BindingAdapter
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.longClicks
import com.zhuzichu.android.mvvm.databinding.BindingCommand
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


@BindingAdapter(
    value = ["onClickCommand", "onLongClickCommand", "onClickViewCommand", "onLongClickViewCommand", "isThrottleFirst"],
    requireAll = false
)
fun onClickCommand(
    view: View,
    clickCommand: BindingCommand<*>?,
    longClickCommand: BindingCommand<*>?,
    onClickViewCommand: BindingCommand<View>?,
    onLongClickViewCommand: BindingCommand<View>?,
    isThrottleFirst: Boolean?
) {
    clickCommand?.apply {
        view.clicks().isThrottleFirst(isThrottleFirst ?: true).subscribe {
            execute()
        }
    }

    longClickCommand?.apply {
        view.longClicks().isThrottleFirst(isThrottleFirst ?: true).subscribe {
            execute()
        }
    }

    onClickViewCommand?.apply {
        view.longClicks().isThrottleFirst(isThrottleFirst ?: true).subscribe {
            execute(view)
        }
    }

    onLongClickViewCommand?.apply {
        view.longClicks().isThrottleFirst(isThrottleFirst ?: true).subscribe {
            execute(view)
        }
    }
}

private fun <T> Observable<T>.isThrottleFirst(
    isThrottleFirst: Boolean
): Observable<T> {
    return this.compose<T> {
        if (isThrottleFirst) {
            it.throttleFirst(150L, TimeUnit.MILLISECONDS)
        } else {
            it
        }
    }
}

@BindingAdapter(value = ["displayedChild"], requireAll = false)
fun bindViewGroup(viewGroup: ViewGroup, position: Int?) {
    viewGroup.forEachIndexed { index, view ->
        if (position == index) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter(value = ["visibility", "isShown"], requireAll = false)
fun bindViewVisibility(view: View, visibility: Int?, isShown: Boolean?) {
    visibility?.let {
        view.visibility = visibility
    }

    isShown?.let {
        if (isShown) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter(value = ["currentView"], requireAll = false)
fun replyCurrentView(currentView: View, bindingCommand: BindingCommand<*>?) {
    bindingCommand?.execute(currentView)
}
