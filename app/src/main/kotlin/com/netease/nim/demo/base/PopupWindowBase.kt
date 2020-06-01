package com.netease.nim.demo.base

import android.content.Context
import android.view.View
import razerdp.basepopup.BasePopupWindow

/**
 * desc popupwinndow基类
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
abstract class PopupWindowBase(context: Context) : BasePopupWindow(context) {

    abstract fun setLayoutId(): Int

    abstract fun initView()

    open fun onCreateView(view: View): View {
        return view
    }

    override fun onCreateContentView(): View {
        return onCreateView(createPopupById(setLayoutId()))
    }

    fun show() {
        initView()
        showPopupWindow()
    }

    fun show(anchorView: View) {
        initView()
        showPopupWindow(anchorView)
    }

    fun show(x: Int, y: Int) {
        initView()
        showPopupWindow(x, y)
    }
}