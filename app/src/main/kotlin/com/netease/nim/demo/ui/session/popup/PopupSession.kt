package com.netease.nim.demo.ui.session.popup

import android.content.Context
import android.widget.TextView
import com.netease.nim.demo.R
import com.netease.nim.demo.base.PopupWindowBase
import com.netease.nim.demo.ui.session.viewmodel.ItemViewModelSession

class PopupSession(
    context: Context,
    private val session: ItemViewModelSession
) :
    PopupWindowBase(context) {
    private lateinit var tvTop: TextView
    override fun setLayoutId(): Int {
        return R.layout.popup_session
    }

    override fun showPopupWindow() {
        super.showPopupWindow()
        tvTop = findViewById(R.id.top)
        val isTop = session.isTop.value ?: false
        tvTop.text = if (isTop) "取消置顶" else "置顶"
        tvTop.setOnClickListener {
            if (isTop) {
                session.unTop()
            } else {
                session.top()
            }
            dismiss()
        }
    }

}