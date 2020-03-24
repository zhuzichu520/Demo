package com.zhuzichu.android.shared.databinding.textview

import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.nex3z.notificationbadge.NotificationBadge
import com.zhuzichu.android.shared.ext.ParseDateFormat

@BindingAdapter(value = ["parseDataFromString"], requireAll = false)
fun parseDataFromString(textView: TextView, string: String?) {
    string?.let {
        textView.text = ParseDateFormat.getTimeAgo(it)
    }
}


@BindingAdapter(value = ["htmlText"], requireAll = false)
fun bindingHtmlText(textView: TextView, string: String?) {
    string?.let {
        textView.text = HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

@BindingAdapter(value = ["number"], requireAll = false)
fun bindBadgeText(badge: NotificationBadge, number: Int?) {
    number?.let {
        badge.setNumber(number, false)
    }
}