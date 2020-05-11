package com.hiwitech.android.shared.databinding.view

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.forEachIndexed
import androidx.databinding.BindingAdapter
import com.hiwitech.android.mvvm.databinding.BindingCommand
import com.hiwitech.android.shared.ext.logi
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.view.longClicks
import com.jakewharton.rxbinding3.view.touches
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


@BindingAdapter(
    value =
    [
        "onClickCommand",
        "onLongClickCommand",
        "onClickViewCommand",
        "onLongClickViewCommand",
        "onTouchCommmand",
        "isThrottleFirst"
    ],
    requireAll = false
)
fun onClickCommand(
    view: View,
    clickCommand: BindingCommand<*>?,
    longClickCommand: BindingCommand<*>?,
    onClickViewCommand: BindingCommand<*>?,
    onLongClickViewCommand: BindingCommand<*>?,
    onTouchCommmand: BindingCommand<MotionEvent>?,
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
        view.clicks().isThrottleFirst(isThrottleFirst ?: true).subscribe {
            execute(view)
        }
    }

    onLongClickViewCommand?.apply {
        view.longClicks().isThrottleFirst(isThrottleFirst ?: true).subscribe {
            execute(view)
        }
    }

    onTouchCommmand?.apply {
        view.touches().isThrottleFirst(isThrottleFirst ?: true).subscribe {
            execute(it)
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

@BindingAdapter(value = ["displayChild", "displayChild2", "layoutDirection"], requireAll = false)
fun bindViewGroup(
    viewGroup: ViewGroup,
    displayChild: Int?,
    displayChild2: Int?,
    layoutDirection: Int?
) {

    displayChild?.let {
        it.toString().logi()
        viewGroup.forEachIndexed { index, view ->
            if (it == index) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
    }

    displayChild2?.let {
        viewGroup.forEachIndexed { index, view ->
            if (it == index) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.INVISIBLE
            }
        }
    }

    layoutDirection?.let {
        viewGroup.layoutDirection = layoutDirection
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

@BindingAdapter(value = ["backgroundColor", "background"], requireAll = false)
fun bindViewColor(view: View, backgroundColor: Int?, @DrawableRes background: Int?) {
    backgroundColor?.let {
        view.setBackgroundColor(it)
    }
    background?.let {
        view.setBackgroundResource(it)
    }
}

@BindingAdapter(value = ["layoutWidth", "layoutHeight"], requireAll = false)
fun bindViewLayout(view: View, layoutWidth: Int?, layoutHeight: Int?) {
    layoutWidth?.let {
        view.setBackgroundColor(it)
        view.layoutParams.width = it
    }
    layoutHeight?.let {
        view.layoutParams.height = it
    }
    view.layoutParams = view.layoutParams
}