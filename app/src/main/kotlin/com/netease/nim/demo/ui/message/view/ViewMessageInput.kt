package com.netease.nim.demo.ui.message.view

import android.animation.ValueAnimator
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.libs.tool.*
import com.hiwitech.android.shared.ext.clean
import com.jakewharton.rxbinding3.view.layoutChangeEvents
import com.netease.nim.demo.R
import com.netease.nim.demo.nim.emoji.ToolMoon
import com.netease.nim.demo.storage.NimUserStorage
import com.uber.autodispose.android.autoDispose
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

    /**
     * 类型
     */
    private var inputType: Int = TYPE_DEFAULT

    /**
     * 底部emoji页面高度
     */
//    private val emojiHeight = dp2px(context, 320f)
    /**
     * 底部更多页面高度
     */
//    private val moreHeight = dp2px(context, 240f)

    /**
     * 底部布局的高度与键盘的高度偏移量
     */

    private val keyboardOffset = dp2px(context, 50f)

    /**
     * 根布局
     */
    private lateinit var contentView: View

    /**
     * 消息列表的RecyclerView
     */
    private lateinit var recyclerView: RecyclerView

    /**
     * contentView 的高度，当键盘弹起来，高度会变
     */
    private var contentViewHeight: Int = 0

    /**
     * recyclerView的高度，当键盘弹起来，高度会变
     */
    private var recyclerViewHeight: Int = 0

    /**
     * 底部输入布局，当输入换行时高度会变
     */
    private var inputHeight: Int = 0


    var onClickSendListener: (String.() -> Unit)? = null

    var onReplaceFragment: (Int.() -> Fragment)? = null

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
        setOnClickListener(
            this, start_voice, start_keyboard, center_emoji,
            center_keyboard, end_more, end_send
        )

        center_input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        center_input.addTextChangedListener(object : TextWatcher {

            private var start = 0
            private var count = 0

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                this.start = start
                this.count = count
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                checkSendButtonEnable()
                ToolMoon.replaceEmoticons(context, center_input, start, count)
            }
        })

        center_input.setOnTouchListener { _, motionEvent ->
            if (MotionEvent.ACTION_UP == motionEvent.action) {
                setInputType(TYPE_INPUT)
            }
            false
        }

        layout_input.post {
            layoutChangeEvents().autoDispose(layout_input).subscribe {
                inputHeight = layout_input.height
            }
        }
    }

    fun attachContentView(contentView: View, recyclerView: RecyclerView) {
        this.contentView = contentView
        this.recyclerView = recyclerView
        this.contentViewHeight = contentView.height
        this.recyclerViewHeight = recyclerView.height
        this.inputHeight = layout_input.height
    }

    fun getInputType(): Int {
        return inputType
    }

    /**
     * 设置类型
     *  @param inputType
     */
    fun setInputType(inputType: Int) {
        layout_input.post {
            when (inputType) {
                TYPE_DEFAULT -> {
                    showView(start_voice, center_emoji, center_input)
                    hideView(start_keyboard, center_keyboard, center_audio)
                    layout_input.post {
                        lockRecyclerViewHeight(contentViewHeight - inputHeight)
                        hideView(layout_bottom)
                        closeKeyboard(center_input)
                        unlockRecyclerViewHeight()
                    }
                }
                TYPE_INPUT -> {
                    hideView(start_keyboard, center_keyboard, center_audio)
                    showView(start_voice, center_emoji, center_input)
                    layout_input.post {
                        lockRecyclerViewHeight(contentViewHeight - getSoftKeyboardHeightLocalValue() - inputHeight)
                        hideView(layout_bottom)
                        showSoftKeyboard()
                        unlockRecyclerViewHeight()
                    }
                }
                TYPE_EMOJI -> {
                    showView(start_voice, center_keyboard, center_input)
                    hideView(start_keyboard, center_emoji, center_audio)
                    val emojiHeight = getSoftKeyboardHeightLocalValue() + keyboardOffset
                    layout_input.post {
                        lockRecyclerViewHeight(contentViewHeight - emojiHeight - inputHeight)
                        layout_bottom.layoutParams.height = emojiHeight
                        showView(layout_bottom)
                        closeKeyboard(center_input)
                        onReplaceFragment?.let {
                            val fragment = it.invoke(TYPE_EMOJI)
                            replaceFragment(fragment)
                        }
                        unlockRecyclerViewHeight()
                    }
                }
                TYPE_MORE -> {
                    if (this.inputType == TYPE_MORE) {
                        setInputType(TYPE_INPUT)
                        return@post
                    }
                    showView(start_voice, center_emoji, center_input)
                    hideView(start_keyboard, center_keyboard, center_audio)
                    val moreHeight = getSoftKeyboardHeightLocalValue()
                    layout_input.post {
                        lockRecyclerViewHeight(contentViewHeight - moreHeight - inputHeight)
                        layout_bottom.layoutParams.height = moreHeight
                        showView(layout_bottom)
                        closeKeyboard(center_input)
                        onReplaceFragment?.let {
                            val fragment = it.invoke(TYPE_MORE)
                            replaceFragment(fragment)
                        }
                        unlockRecyclerViewHeight()
                    }
                }
                TYPE_VOICE -> {
                    showView(start_keyboard, center_emoji, center_audio)
                    hideView(start_voice, center_keyboard, center_input)
                    layout_input.post {
                        lockRecyclerViewHeight(contentViewHeight - inputHeight)
                        hideView(layout_bottom)
                        closeKeyboard(center_input)
                        unlockRecyclerViewHeight()
                    }
                }
            }
            checkSendButtonEnable()
            this.inputType = inputType
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val manager = (context as AppCompatActivity).supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.layout_bottom, fragment)
        transaction.commitAllowingStateLoss()
    }

    private fun showSoftKeyboard() {
        showKeyboard(context, center_input)
        MainHandler.postDelayed(350) {
            center_input.apply {
                isFocusable = true
                isFocusableInTouchMode = true
                requestFocus()
            }
        }
    }

    /**
     * 更新输入判断是否显示发送
     */
    private fun checkSendButtonEnable() {
        if (center_input.text.toString().isNotEmpty() && start_voice.visibility == View.VISIBLE) {
            showView(end_send)
            hideView(end_more)
        } else {
            showView(end_more)
            hideView(end_send)
        }
    }

    /**
     * 点击事件
     */
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
                onClickSendListener?.invoke(center_input.text.toString())
                center_input.clean()
            }
        }
    }

    /**
     * 释放锁定RecyclerView的高度
     */
    private fun lockRecyclerViewHeight(height: Int) {
        val layoutParams = recyclerView.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0f
        val valueAnimator = ValueAnimator.ofInt(recyclerView.height, height)
        valueAnimator.addUpdateListener {
            layoutParams.height = toInt(it.animatedValue)
            recyclerView.layoutParams = layoutParams
            scrollToBottom()
        }
        valueAnimator.duration = 200
        valueAnimator.start()
    }

    private fun scrollToBottom() {
        recyclerView.scrollBy(0, Int.MAX_VALUE)
    }

    /**
     * 释放锁定的RecyclerView的高度
     */
    private fun unlockRecyclerViewHeight() {
        MainHandler.postDelayed(250) {
            val layoutParams = recyclerView.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 1f
        }
    }

    private fun getSoftKeyboardHeightLocalValue(): Int {
        return NimUserStorage.softKeyboardHeight
    }

}