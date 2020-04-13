package com.netease.nim.demo.ui.message.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.libs.tool.*
import com.hiwitech.android.shared.ext.toast
import com.netease.nim.demo.R
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.message.emoji.fragment.FragmentEmoji
import com.netease.nim.demo.ui.message.more.fragment.FragmentMore
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
     * todo 内存泄漏 带优化
     * 初始化fragment
     */
    private var fragmentEmoji: FragmentEmoji

    private var fragmentMore: FragmentMore

    /**
     * 类型
     */
    private var inputType: Int = TYPE_DEFAULT

    /**
     * 底部emoji页面高度
     */
    private val emojiHeight = dp2px(context, 320f)
    /**
     * 底部更多页面高度
     */
    private val moreHeight = dp2px(context, 240f)

    private lateinit var contentView: View

    private var contentViewHeight: Int = 0

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
            this,
            start_voice,
            start_keyboard,
            center_emoji,
            center_keyboard,
            end_more
        )
        center_input.addTextChangedListener {
            updateInput()
        }

        center_input.setOnTouchListener { _, motionEvent ->
            if (MotionEvent.ACTION_UP == motionEvent.action) {
                setInputType(TYPE_INPUT)
            }
            true
        }
        fragmentEmoji = FragmentEmoji()
        fragmentMore = FragmentMore()
    }

    fun attachContentView(view: View) {
        this.contentView = view
        this.contentViewHeight = contentView.height
    }

    fun getInputType(): Int {
        return inputType
    }

    /**
     * 设置类型
     *  @param inputType
     */
    fun setInputType(inputType: Int) {
        when (inputType) {
            TYPE_DEFAULT -> {
                lockRecyclerViewHeight(contentViewHeight)
                showView(start_voice, center_emoji, center_input)
                hideView(start_keyboard, center_keyboard, center_audio, layout_bottom)
                closeKeyboard(center_input)
                unlockRecyclerViewHeight()
            }
            TYPE_INPUT -> {
                lockRecyclerViewHeight(contentViewHeight - getSoftKeyboardHeightLocalValue())
                showView(start_voice, center_emoji, center_input)
                hideView(start_keyboard, center_keyboard, center_audio, layout_bottom)
                showSoftKeyboard()
                unlockRecyclerViewHeight()
            }
            TYPE_EMOJI -> {
                layout_bottom.layoutParams.height = emojiHeight
                lockRecyclerViewHeight(contentViewHeight - emojiHeight)
                showView(start_voice, center_keyboard, center_input, layout_bottom)
                hideView(start_keyboard, center_emoji, center_audio)
                closeKeyboard(center_input)
                replaceFragment(fragmentEmoji)
                unlockRecyclerViewHeight()
            }
            TYPE_MORE -> {
                if (this.inputType == TYPE_MORE) {
                    setInputType(TYPE_INPUT)
                    return
                }
                layout_bottom.layoutParams.height = moreHeight
                lockRecyclerViewHeight(contentViewHeight - moreHeight)
                showView(start_voice, center_emoji, center_input, layout_bottom)
                hideView(start_keyboard, center_keyboard, center_audio)
                closeKeyboard(center_input)
                replaceFragment(fragmentMore)
                unlockRecyclerViewHeight()
            }
            TYPE_VOICE -> {
                lockRecyclerViewHeight(contentViewHeight)
                showView(start_keyboard, center_emoji, center_audio)
                hideView(start_voice, center_keyboard, center_input, layout_bottom)
                closeKeyboard(center_input)
                unlockRecyclerViewHeight()
            }
        }
        updateInput()
        this.inputType = inputType
    }

    private fun replaceFragment(fragment: Fragment) {
        val manager = (context as AppCompatActivity).supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.layout_bottom, fragment)
        transaction.commitAllowingStateLoss()
    }

    private fun showSoftKeyboard() {
        showKeyboard(context, center_input)
        MainHandler.postDelayed { getSoftKeyboardHeight().toString().toast() }
    }

    /**
     * 更新输入判断是否显示发送
     */
    private fun updateInput() {
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

            }
        }
    }

    /**
     * 释放锁定RecyclerView的高度
     */
    private fun lockRecyclerViewHeight(height: Int) {
        val layoutParams = contentView.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0f
        val valueAnimator = ValueAnimator.ofInt(contentView.height, height)
        valueAnimator.addUpdateListener {
            layoutParams.height = toInt(it.animatedValue)
            contentView.layoutParams = layoutParams
            scrollToBottom()
        }
        valueAnimator.duration = 200
        valueAnimator.start()
    }

    private fun scrollToBottom() {
        (contentView as RecyclerView).scrollToPosition((contentView as RecyclerView).adapter!!.itemCount - 1)
    }

    /**
     * 释放锁定的RecyclerView的高度
     */
    private fun unlockRecyclerViewHeight() {
        MainHandler.postDelayed(300) {
            val layoutParams = contentView.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 1f
        }
    }


    private fun getSoftKeyboardHeight(): Int {
        return (getSoftKeyboardHeight(context.toCast())).apply {
            if (this != 0) {
                NimUserStorage.softKeyboardHeight = this
            }
        }
    }

    private fun getSoftKeyboardHeightLocalValue(): Int {
        return NimUserStorage.softKeyboardHeight
    }

}