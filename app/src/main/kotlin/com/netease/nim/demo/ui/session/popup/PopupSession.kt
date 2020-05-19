package com.netease.nim.demo.ui.session.popup

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.hiwitech.android.libs.tool.setOnClickListener
import com.hiwitech.android.shared.ext.toStringByResId
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
    PopupWindowBase(context), View.OnClickListener {

    private lateinit var top: TextView
    private lateinit var disturb: TextView
    private lateinit var remove: TextView

    private var isTop = false

    override fun onCreateView(view: View) {
        width = WRAP_CONTENT
        height = WRAP_CONTENT
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        setBackgroundColor(android.R.color.transparent)
    }

    override fun setLayoutId(): Int {
        return R.layout.popup_session
    }

    override fun initView() {
        top = findViewById(R.id.top)
        disturb = findViewById(R.id.disturb)
        remove = findViewById(R.id.remove)
        setOnClickListener(this, top, disturb, remove)
        isTop = session.isTop.value ?: false
        top.text = if (isTop)
            R.string.clear_sticky_on_top.toStringByResId()
        else
            R.string.sticky_on_top.toStringByResId()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.top -> {
                changeTop()
            }
            R.id.disturb -> {

            }
            R.id.remove -> {
                session.viewModel.deleteSession(session)
            }
        }
        dismiss()
    }

    private fun changeTop() {
        if (isTop) {
            session.unTop()
        } else {
            session.top()
        }
    }

}