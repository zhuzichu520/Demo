package com.netease.nim.demo.ui.session.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.netease.nim.demo.R
import com.hiwitech.android.widget.badge.Badge
import com.hiwitech.android.widget.badge.QBadgeView

class ViewSessionBadge @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var badge: Badge

    var onDragStateChangedListener: Badge.OnDragStateChangedListener? = null
        set(value) {
            field = value
            badge.setOnDragStateChangedListener(field)
        }

    var number: Int = 0
        set(value) {
            field = value
            badge.badgeNumber = field
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_session_badge, this)
        badge = QBadgeView(context).bindTarget(findViewById(R.id.target))
        badge.badgeGravity = Gravity.TOP or Gravity.END
        badge.setGravityOffset(16f, 30f, true)
    }
}