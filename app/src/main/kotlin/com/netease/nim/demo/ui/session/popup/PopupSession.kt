package com.netease.nim.demo.ui.session.popup

import android.content.Context
import com.netease.nim.demo.R
import com.netease.nim.demo.base.PopupWindowBase

class PopupSession(context: Context) : PopupWindowBase(context) {

    override fun setLayoutId(): Int = R.layout.popup_session
}