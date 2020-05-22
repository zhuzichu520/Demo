package com.hiwitech.android.shared.databinding.textview

import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.hiwitech.android.mvvm.databinding.BindingCommand
import com.hiwitech.android.shared.ext.ParseDateFormat
import com.hiwitech.android.widget.qmui.textview.QMUILinkTextView

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

@BindingAdapter(value = ["textColor", "text"], requireAll = false)
fun bindingTextView(textView: TextView, color: Int?, text: Int?) {
    color?.let {
        textView.setTextColor(it)
    }
    text?.let {
        textView.setText(text)
    }
}

@BindingAdapter(
    value = ["onClickLinkTelCommand", "onClickLinkEmailConmmnd", "onClickLinkUrlCommand"],
    requireAll = false
)
fun bindingQmuiTextView(
    qmuiLinkTextView: QMUILinkTextView,
    onClickTelCommand: BindingCommand<String>?,
    onClickEmailConmmnd: BindingCommand<String>?,
    onClickUrlCommand: BindingCommand<String>?
) {
    qmuiLinkTextView.setOnLinkClickListener(object : QMUILinkTextView.OnLinkClickListener {

        override fun onMailLinkClick(mailAddress: String?) {
            onClickEmailConmmnd?.execute(mailAddress)
        }

        override fun onWebUrlLinkClick(url: String?) {
            onClickUrlCommand?.execute(url)
        }

        override fun onTelLinkClick(phoneNumber: String?) {
            onClickTelCommand?.execute(phoneNumber)
        }

    })
}