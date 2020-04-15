package com.hiwitech.android.shared.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/15 6:21 PM
 * since: v 1.0.0
 */
class SquareRelativeLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            getDefaultSize(0, widthMeasureSpec),
            getDefaultSize(0, heightMeasureSpec)
        )
        val childWidthSize = measuredWidth
        val measureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY)
        super.onMeasure(measureSpec, measureSpec)
    }

}