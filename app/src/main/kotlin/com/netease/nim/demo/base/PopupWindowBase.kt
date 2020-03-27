package com.netease.nim.demo.base

import android.content.Context
import android.view.View
import razerdp.basepopup.BasePopupWindow

abstract class PopupWindowBase(context: Context) : BasePopupWindow(context) {

    abstract fun setLayoutId(): Int

    override fun onCreateContentView(): View {
        return createPopupById(setLayoutId())
    }
}