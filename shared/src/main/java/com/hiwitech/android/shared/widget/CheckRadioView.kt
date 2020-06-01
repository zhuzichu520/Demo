package com.hiwitech.android.shared.widget

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.hiwitech.android.shared.R

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/27 9:37 PM
 * since: v 1.0.0
 */
class CheckRadioView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    lateinit var radioDrawable: Drawable
    private var selectedColor: Int = -1
    private var unSelectedColor: Int = -1

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CheckRadioView)
        selectedColor =
            typedArray.getColor(R.styleable.CheckRadioView_CheckRadioView_SelectedColor, 0)
        unSelectedColor =
            typedArray.getColor(R.styleable.CheckRadioView_CheckRadioView_UnSelectUdColor, 0)
        typedArray.recycle()
        setChecked(false)
    }

    fun setChecked(enable: Boolean) {
        if (enable) {
            setImageResource(com.zhihu.matisse.R.drawable.ic_preview_radio_on)
            radioDrawable = drawable
            radioDrawable.setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN)
        } else {
            setImageResource(com.zhihu.matisse.R.drawable.ic_preview_radio_off)
            radioDrawable = drawable
            radioDrawable.setColorFilter(unSelectedColor, PorterDuff.Mode.SRC_IN)
        }
    }

}