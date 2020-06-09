package com.netease.nim.demo.ui.message.view

import android.Manifest
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
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.libs.tool.*
import com.hiwitech.android.shared.bus.LiveDataBus
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.hiwitech.android.shared.ext.clean
import com.jakewharton.rxbinding3.view.layoutChangeEvents
import com.netease.nim.demo.R
import com.netease.nim.demo.nim.emoji.ToolMoon
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.message.emoticon.fragment.FragmentEmoticon
import com.netease.nim.demo.ui.message.main.event.EventMessage
import com.netease.nim.demo.ui.message.more.fragment.FragmentMore
import com.netease.nim.demo.ui.permissions.fragment.FragmentPermissions
import com.netease.nimlib.sdk.media.record.AudioRecorder
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uber.autodispose.android.autoDispose
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.layout_input.view.*
import java.util.concurrent.TimeUnit

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/13 9:38 AM
 * since: v 1.0.0
 * todo 待优化，还是不太流畅
 */
class ViewMessageInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {

    /**
     * 动画时间
     */
    private val duration = 150L

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
    private val moreHeight = dp2px(context, 320f)

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

    /**
     * emoji的Fragment
     */
    private val fragmentEmoji = FragmentEmoticon()

    /**
     * 更多的Fragment
     */
    private val fragmentMore = FragmentMore()

    private var currentFragment: Fragment? = null

    /**
     * 录音秒数
     */
    private var recordSecond = 0
    /**
     * 是否打开录音
     */
    private var isOpenRecord = false

    /**
     * 是否能取消录音
     */
    private var cancelled: Boolean = false

    /**
     * 计时器的Disposable对象
     */
    private var disposableTime: Disposable? = null

    private var fragmentManager: FragmentManager =
        (context as AppCompatActivity).supportFragmentManager

    var audioRecorder: AudioRecorder? = null

    private var fragmentPermissions: FragmentPermissions = FragmentPermissions()

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
        //加载布局
        LayoutInflater.from(context).inflate(R.layout.layout_input, this)
        //设置点击事件
        setOnClickListener(
            this, start_voice, start_keyboard, center_emoji,
            center_keyboard, end_more, end_send
        )

        center_input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

        //监听输入变化
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
            true
        }

        //监听输入框高度变化
        layout_input.post {
            layout_input.layoutChangeEvents().autoDispose(layout_input).subscribe {
                inputHeight = layout_input.height
            }
        }

        center_audio.post {
            center_audio.setOnTouchListener { view, motionEvent ->
                RxPermissions(context as FragmentActivity).request(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).autoDispose(this).subscribe {
                    if (it) {
                        setInputType(TYPE_VOICE)
                        when (motionEvent.action) {
                            MotionEvent.ACTION_UP and MotionEvent.ACTION_CANCEL -> {
                                stopRecord(isCancelled(view, motionEvent))
                            }
                            MotionEvent.ACTION_DOWN -> {
                                startRecord()
                            }
                            MotionEvent.ACTION_MOVE -> {
                                updateCancel(isCancelled(view, motionEvent))
                            }
                        }
                    } else {
                        showPermissionsDiaog(context)
                    }
                }
                false
            }
        }
    }

    private fun showPermissionsDiaog(context: FragmentActivity) {
        fragmentPermissions.show("录音，文件读写", context.supportFragmentManager)
    }


    /**
     * 是否计时器的资源
     */
    fun releaseTimeDisposable() {
        disposableTime?.dispose()
        disposableTime = null
    }

    /**
     * 开始计时
     */
    private fun startTime() {
        disposableTime = Flowable.interval(0, 1, TimeUnit.SECONDS)
            .bindToSchedulers()
            .subscribe {
                if (isOpenRecord) {
                    LiveDataBus.post(
                        EventMessage.OnRecordAudioEvent(
                            EventMessage.RECORD_RECORDING,
                            recordSecond++
                        )
                    )
                }
            }

    }

    /**
     * 更新cancelled值
     */
    private fun updateCancel(cancelled: Boolean) {
        if (!isOpenRecord)
            return
        if (this.cancelled == cancelled)
            return
        this.cancelled = cancelled
        LiveDataBus.post(EventMessage.OnRecordCancelChangeEvent(this.cancelled))
    }

    /**
     * 是否取消
     */
    private fun isCancelled(view: View, event: MotionEvent): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return event.rawX < location[0] || event.rawX > location[0] + view.width || event.rawY < location[1] - 40
    }

    /**
     * 停止录音
     * @param isCancelled 是否取消发送这段录音
     */
    private fun stopRecord(isCancelled: Boolean) {
        releaseTimeDisposable()
        recordSecond = 0
        isOpenRecord = false
        audioRecorder?.completeRecord(isCancelled)
        center_layout.setBackgroundResource(R.drawable.nim_input_edit)
        if (isCancelled) {
            LiveDataBus.post(
                EventMessage.OnRecordAudioEvent(
                    EventMessage.RECORD_CANCEL,
                    recordSecond
                )
            )
        } else {
            LiveDataBus.post(
                EventMessage.OnRecordAudioEvent(
                    EventMessage.RECORD_SEND,
                    recordSecond
                )
            )
        }
    }

    /**
     * 录音
     */
    private fun startRecord() {
        releaseTimeDisposable()
        recordSecond = 0
        isOpenRecord = true
        cancelled = false
        audioRecorder?.startRecord()
        center_layout.setBackgroundResource(R.drawable.nim_input_edit_pressed)
        LiveDataBus.post(EventMessage.OnRecordCancelChangeEvent(this.cancelled))
        LiveDataBus.post(
            EventMessage.OnRecordAudioEvent(
                EventMessage.RECORD_RECORDING,
                recordSecond
            )
        )
        startTime()
    }

    /**
     * 初始化View
     */
    fun attachContentView(contentView: View, recyclerView: RecyclerView) {
        this.contentView = contentView
        this.recyclerView = recyclerView
        this.contentViewHeight = contentView.height
        this.recyclerViewHeight = recyclerView.height
        this.inputHeight = layout_input.height
    }

    /**
     * 获取底部显示的状态类型
     */
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
                TYPE_DEFAULT -> {// 1969   160   771
                    showView(start_voice, center_emoji, center_input)
                    hideView(start_keyboard, center_keyboard, center_audio)
                    layout_input.post {
                        lockRecyclerViewHeight(contentViewHeight - inputHeight)
                        showBottom(false)
                        hideSoftKeyboard()
                        unlockRecyclerViewHeight()
                    }
                }
                TYPE_INPUT -> {
                    hideView(start_keyboard, center_keyboard, center_audio)
                    showView(start_voice, center_emoji, center_input)
                    layout_input.post {
                        lockRecyclerViewHeight(contentViewHeight - getSoftKeyboardHeightLocalValue() - inputHeight)
                        showBottom(false)
                        showSoftKeyboard()
                        unlockRecyclerViewHeight()
                    }
                }
                TYPE_EMOJI -> {
                    showView(start_voice, center_keyboard, center_input)
                    hideView(start_keyboard, center_emoji, center_audio)
//                    val emojiHeight = getSoftKeyboardHeightLocalValue() + keyboardOffset
                    layout_input.post {
                        lockRecyclerViewHeight(contentViewHeight - emojiHeight - inputHeight)
                        layout_bottom.layoutParams.height = emojiHeight
                        showBottom(true)
                        hideSoftKeyboard()
                        replaceFragment(fragmentEmoji)
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
//                    val moreHeight = getSoftKeyboardHeightLocalValue()
                    layout_input.post {
                        lockRecyclerViewHeight(contentViewHeight - moreHeight - inputHeight)
                        layout_bottom.layoutParams.height = moreHeight
                        showBottom(true)
                        hideSoftKeyboard()
                        replaceFragment(fragmentMore)
                        unlockRecyclerViewHeight()
                    }
                }
                TYPE_VOICE -> {
                    showView(start_keyboard, center_emoji, center_audio)
                    hideView(start_voice, center_keyboard, center_input)
                    layout_input.post {
                        lockRecyclerViewHeight(contentViewHeight - inputHeight)
                        showBottom(false)
                        hideSoftKeyboard()
                        unlockRecyclerViewHeight()
                    }
                }
            }
            checkSendButtonEnable()
            this.inputType = inputType
        }
    }

    /**
     * 切换Fragment
     */
    private fun replaceFragment(fragment: Fragment) {
        if (fragment == currentFragment)
            return
        val transaction = fragmentManager.beginTransaction()
        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.layout_bottom, fragment)
        }
        currentFragment?.let {
            transaction.hide(it)
        }
        transaction.commit()
        this.currentFragment = fragment
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

    private fun hideSoftKeyboard() {
        closeKeyboard(center_input)
    }

    /**
     * 弹出软键盘
     */
    private fun showSoftKeyboard() {
        center_input.apply {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
        MainHandler.postDelayed(duration) {
            showKeyboard(context, center_input)
        }
    }

    private fun showBottom(isShown: Boolean) {
        val floatArray = if (isShown) floatArrayOf(0f, 1f) else floatArrayOf(1f, 0f)
        alpha(layout_bottom, duration, f = *floatArray)
        MainHandler.postDelayed(duration) {
            if (isShown) {
                showView(layout_bottom)
            } else {
                hideView(layout_bottom)
            }
        }
    }

    /**
     * 锁定RecyclerView的高度
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
        valueAnimator.duration = duration
        valueAnimator.start()
    }

    private fun scrollToBottom() {
        recyclerView.scrollBy(0, Int.MAX_VALUE)
    }

    /**
     * 释放锁定的RecyclerView的高度
     */
    private fun unlockRecyclerViewHeight() {
        MainHandler.postDelayed(200) {
            val layoutParams = recyclerView.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 1f
        }
    }

    /**
     * 获取键盘高度
     */
    private fun getSoftKeyboardHeightLocalValue(): Int {
        return NimUserStorage.softKeyboardHeight
    }

}