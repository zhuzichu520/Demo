package com.netease.nim.demo.ui.message.main.fragment

import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.libs.tool.*
import com.hiwitech.android.shared.bus.RxBus
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentMeBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.netease.nim.demo.ui.message.main.viewmodel.ViewModelMessage
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.uber.autodispose.autoDispose
import kotlinx.android.synthetic.main.fragment_message.*
import javax.inject.Inject


/**
 * desc 消息界面Frqagment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentMessage : FragmentBase<FragmentMeBinding, ViewModelMessage, ArgMessage>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_message

    private lateinit var sessionType: SessionTypeEnum

    @Inject
    lateinit var msgService: MsgService

    override fun initArgs(arg: ArgMessage) {
        super.initArgs(arg)
        sessionType = SessionTypeEnum.typeOfValue(arg.sessionType)
    }

    override fun initView() {
        super.initView()
        et_input.setOnTouchListener { _, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                if (!isSoftKeyboardShown())
                    viewModel.onClickCenterKeyboardCommand.execute()
            }
            true
        }
        showKeyboard(true)
    }

    override fun initLazyData() {
        super.initLazyData()
        loadData(sessionType)
        viewModel.loadMessage(
            MessageBuilder.createEmptyMessage(arg.contactId, sessionType, 0),
            isFirst = true
        )
    }

    /**
     * 加载相应的会话数据
     * @param sessionType 会话类型
     */
    private fun loadData(sessionType: SessionTypeEnum) {
        when (sessionType) {
            SessionTypeEnum.P2P -> {
                viewModel.loadUserInfo()
            }
            SessionTypeEnum.Team -> {
                viewModel.loadTeamInfo()
            }
            else -> {

            }
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.onKeyboardVoiceChangeEvent.observe(viewLifecycleOwner, Observer {
            if (!viewModel.isInitLazyView)
                return@Observer
            when (it) {
                ViewModelMessage.TYPE_LEFT_VOICE -> {
                    //点击了键盘
                    if (!isSoftKeyboardShown() && ViewModelMessage.TYPE_CENTER_EMOJI == viewModel.onKeyboardEmojiChangeEvent.value) {
                        showKeyboard()
                    }
                }
                ViewModelMessage.TYPE_LEFT_KEYBOARD -> {
                    //点击了语音
                    if (isSoftKeyboardShown()) {
                        hideKeyboard()
                    } else {
                        hideBottom()
                    }
                    if (ViewModelMessage.TYPE_CENTER_KEYBOARD == viewModel.onKeyboardEmojiChangeEvent.value) {
                        viewModel.onKeyboardEmojiChangeEvent.value =
                            ViewModelMessage.TYPE_CENTER_EMOJI
                    }
                }
            }
            viewModel.input.value = viewModel.input.value
        })

        viewModel.onKeyboardEmojiChangeEvent.observe(viewLifecycleOwner, Observer {
            if (!viewModel.isInitLazyView)
                return@Observer
            when (it) {
                ViewModelMessage.TYPE_CENTER_EMOJI -> {
                    //点击了键盘
                    if (!isSoftKeyboardShown() && ViewModelMessage.TYPE_LEFT_VOICE == viewModel.onKeyboardVoiceChangeEvent.value) {
                        showKeyboard()
                    }
                }
                ViewModelMessage.TYPE_CENTER_KEYBOARD -> {
                    showBottomEmoji()
                    if (ViewModelMessage.TYPE_LEFT_KEYBOARD == viewModel.onKeyboardVoiceChangeEvent.value) {
                        viewModel.onKeyboardVoiceChangeEvent.value =
                            ViewModelMessage.TYPE_LEFT_VOICE
                    }
                }
            }
        })

        viewModel.onBottomChangeEvent.observe(viewLifecycleOwner, Observer {
            when (it) {
                ViewModelMessage.TYPE_BOTTOM_HIDE -> {
                    layout_bottom.visibility = View.GONE
                }
                ViewModelMessage.TYPE_BOTTOM_EMOJI -> {
                    //点击了Emoji
                    val emojiHeight = dp2px(requireContext(), 320f)
                    layout_bottom.layoutParams.height = emojiHeight
                    if (isSoftKeyboardShown()) {
                        lockRecyclerViewHeight(
                            layout_content.bottom - recycler.top - layout_input.height +
                                    getSoftKeyboardHeightLocalValue() - emojiHeight
                        )
                        hideKeyboard()
                    } else {

                    }
                    layout_bottom.visibility = View.VISIBLE
                    unlockRecyclerViewHeight()
                }
                ViewModelMessage.TYPE_BOTTOM_MORE -> {
                    //点击了More
                    val emojiHeight = getSoftKeyboardHeightLocalValue()
                    layout_bottom.layoutParams.height = emojiHeight
                    lockRecyclerViewHeight(
                        layout_content.bottom - recycler.top - layout_input.height-emojiHeight
                    )
                    if (isSoftKeyboardShown()) {
                        hideKeyboard()
                    }
                    layout_bottom.visibility = View.VISIBLE
                    unlockRecyclerViewHeight()
                }
            }
        })

        /**
         * 输入内容监听事件
         */
        viewModel.input.observe(viewLifecycleOwner, Observer {
            if (
                !it.isNullOrEmpty() &&
                ViewModelMessage.TYPE_LEFT_VOICE == viewModel.onKeyboardVoiceChangeEvent.value
            ) {
                viewModel.onMoreSendChangeEvent.value = ViewModelMessage.TYPE_RIGHT_SEND
            } else {
                viewModel.onMoreSendChangeEvent.value = ViewModelMessage.TYPE_RIGHT_MORE
            }
        })

        /**
         * recyclerview滑动到指定位置
         */
        viewModel.onScrollPositionsEvent.observe(viewLifecycleOwner, Observer {
            scrollToPosition(it)
        })
        /**
         * 消息接收监听
         */
        RxBus.toObservable(NimEvent.OnReceiveMessageEvent::class.java)
            .bindToSchedulers()
            .autoDispose(viewModel)
            .subscribe {
                viewModel.addMessage(it.list)
            }
        /**
         * 添加数据完成
         */
        viewModel.onAddMessageCompletedEvent.observe(viewLifecycleOwner, Observer {
            if (isLastVisible()) {
                //最后一条可见 滑动到底部
                MainHandler.postDelayed { scrollToPosition(it.size - 1) }
            } else {
                //todo zhuzichu 最后一条不可见 显示提醒有新消息
            }
        })
    }

    private fun hideBottom() {
        viewModel.onBottomChangeEvent.value = ViewModelMessage.TYPE_BOTTOM_HIDE
    }

    private fun showBottomEmoji() {
        viewModel.onBottomChangeEvent.value = ViewModelMessage.TYPE_BOTTOM_EMOJI
        MainHandler.postDelayed {
            scrollToPosition(viewModel.items.size - 1)
        }
    }

    /**
     * 释放锁定RecyclerView的高度
     */
    private fun lockRecyclerViewHeight(height: Int = recycler.height) {
        val layoutParams = recycler.layoutParams as LinearLayout.LayoutParams
        layoutParams.height = height
        layoutParams.weight = 0f
    }

    /**
     * 释放锁定的RecyclerView的高度
     */
    private fun unlockRecyclerViewHeight() {
        MainHandler.postDelayed {
            val layoutParams = recycler.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 1f
        }
    }

    private fun showKeyboard(isSave: Boolean = false) {
        MainHandler.postDelayed {
            lockRecyclerViewHeight(layout_content.bottom - recycler.top - layout_input.height - getSoftKeyboardHeightLocalValue())
            hideBottom()
            showKeyboard(requireContext(), et_input)
            unlockRecyclerViewHeight()
        }
        MainHandler.postDelayed {
            scrollToPosition(viewModel.items.size - 1)
        }
    }

    private fun hideKeyboard() {
        closeKeyboard(et_input)
    }

    private fun getSoftKeyboardHeight(): Int {
        return (getSoftKeyboardHeight(requireActivity()) + getStatusBarHeight(requireActivity())).apply {
            if (this != 0) {
                NimUserStorage.softKeyboardHeight = this
            }
        }
    }

    private fun getSoftKeyboardHeightLocalValue(): Int {
        return NimUserStorage.softKeyboardHeight
    }

    private fun isSoftKeyboardShown(): Boolean {
        return getSoftKeyboardHeight() != 0
    }

    override fun onResume() {
        super.onResume()
        msgService.setChattingAccount(arg.contactId, sessionType)
    }


    override fun onPause() {
        super.onPause()
        msgService.setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None)
        et_input.clearFocus()
    }

    /**
     * 滑动到置顶位置
     */
    private fun scrollToPosition(position: Int) {
        if (position >= 0) {
            (recycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
        }
    }

    /**
     * 判断最后一条是否可见
     */
    private fun isLastVisible(): Boolean {
        val position = viewModel.items.size - 1
        val findLastPosition =
            (recycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        return findLastPosition == position
    }

}