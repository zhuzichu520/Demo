package com.netease.nim.demo.ui.message.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.hiwitech.android.libs.tool.*
import com.netease.nim.demo.R
import kotlinx.android.synthetic.main.layout_input.view.*

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/13 9:38 AM
 * since: v 1.0.0
 */
class ViewMessageInput @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var inputType: Int = TYPE_DEFAULT

    private val emojiHeight = dp2px(context, 320f)
    private val moreHeight = dp2px(context, 280f)

    companion object {
        //默认状态 无键盘
        const val TYPE_DEFAULT = 0
        //输入状态 有键盘
        const val TYPE_INPUT = 1
        //显示emoji状态 无键盘
        const val TYPE_EMOJI = 2
        //显示更多 无键盘
        const val TYPE_MORE = 3
        //显示语言 无键盘
        const val TYPE_VOICE = 4
    }


    init {
        LayoutInflater.from(context).inflate(R.layout.layout_input, this)
        setOnClickListener(this, start_voice, start_keyboard, center_emoji, center_keyboard)
        center_input.addTextChangedListener {
            updateInput()
        }
        setInputType(TYPE_DEFAULT)
    }

    private fun setInputType(inputType: Int) {
        when (inputType) {
            TYPE_DEFAULT -> {
                showView(start_voice, center_emoji, center_input)
                hideView(start_keyboard, center_keyboard, center_audio)
                closeKeyboard(center_input)
            }
            TYPE_INPUT -> {
                showView(start_voice, center_emoji, center_input)
                hideView(start_keyboard, center_keyboard, center_audio)
                showKeyboard(context, center_input)
            }
            TYPE_EMOJI -> {
                showView(start_voice, center_keyboard, center_input)
                hideView(start_keyboard, center_emoji, center_audio)
                closeKeyboard(center_input)
            }
            TYPE_MORE -> {
                showView(start_voice, center_emoji, center_input)
                hideView(start_keyboard, center_keyboard, center_audio)
                closeKeyboard(center_input)
            }
            TYPE_VOICE -> {
                showView(start_keyboard, center_emoji, center_audio)
                hideView(start_voice, center_keyboard, center_input)
                closeKeyboard(center_input)
            }
        }
        updateInput()
        this.inputType = inputType
    }

    private fun updateInput() {
        if (center_input.text.toString().isNotEmpty() && start_voice.visibility == View.VISIBLE) {
            showView(end_send)
            hideView(end_more)
        } else {
            showView(end_more)
            hideView(end_send)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.start_voice -> {
                setInputType(TYPE_VOICE)
            }
            R.id.start_keyboard -> {
                setInputType(TYPE_INPUT)
            }
            R.id.center_emoji -> {
                setInputType(TYPE_EMOJI)
            }
            R.id.center_keyboard -> {
                setInputType(TYPE_INPUT)
            }
            R.id.end_more -> {
                setInputType(TYPE_MORE)
            }
            R.id.end_send -> {

            }
        }
    }
}