package com.netease.nim.demo.ui.session.popup

import android.content.Context
import android.widget.TextView
import com.netease.nim.demo.R
import com.netease.nim.demo.base.PopupWindowBase
import com.netease.nim.demo.ui.session.viewmodel.ItemViewModelSession

/**
 * desc 会话列表长点击弹窗
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
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