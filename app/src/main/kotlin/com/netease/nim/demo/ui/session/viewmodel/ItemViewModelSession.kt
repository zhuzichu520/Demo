package com.netease.nim.demo.ui.session.viewmodel

import android.view.Gravity
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.createTypeCommand
import com.hiwitech.android.shared.tools.ToolDate
import com.hiwitech.android.widget.badge.Badge
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.nim.tools.ToolTeam
import com.netease.nim.demo.nim.tools.ToolUserInfo
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.netease.nim.demo.ui.session.Constants.SESSION_ON_TOP
import com.netease.nim.demo.ui.session.popup.PopupSession
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum.P2P
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum.Team
import com.netease.nimlib.sdk.msg.model.RecentContact

/**
 * desc 消息列表Item的ViewModel
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
data class ItemViewModelSession(
    val viewModel: ViewModelSession,
    val contact: RecentContact
) : ItemViewModelBase(viewModel) {

    private val contactId = contact.contactId
    val time = contact.time
    /**
     * 头像
     */
    val avatar = MutableLiveData<Any>()
    /**
     * 会话标题
     */
    val name = MutableLiveData<String>()
    /**
     * 头像错误图片
     */
    val error = MutableLiveData<Int>()
    /**
     * 头像占位图
     */
    val placeholder = MutableLiveData<Int>()
    /**
     * 会话内容
     */
    val content = MutableLiveData<String>()
    /**
     * 会话时间
     */
    val date = MutableLiveData<String>()
    /**
     * 置顶标记
     */
    val isTop = MutableLiveData<Boolean>()
    /**
     * 会话未读书
     */
    val number = MutableLiveData<Int>()

    /**
     * 初始化数据
     */
    init {
        val contactId = contact.contactId
        content.value = contact.content
        date.value = ToolDate.getTimeShowString(contact.time, false)
        isTop.value = isOnTop()
        number.value = contact.unreadCount
        when (contact.sessionType) {
            P2P -> {
                val userInfo = ToolUserInfo.getUserInfo(contactId)
                name.value = userInfo.name
                avatar.value = userInfo.avatar
                error.value = R.mipmap.nim_avatar_default
                placeholder.value = R.mipmap.nim_avatar_default
            }
            Team -> {
                val team = ToolTeam.getTeam(contactId)
                name.value = team.name
                avatar.value = team.icon
                error.value = R.mipmap.nim_avatar_group
                placeholder.value = R.mipmap.nim_avatar_group
            }
            else -> {

            }
        }
    }

    /**
     * 小红点拖动处理
     */
    val onDragStateChangedCommamnd = createTypeCommand<Int> {
        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == this) {
            viewModel.msgService.clearUnreadCount(contactId, contact.sessionType)
        }
    }

    /**
     * 会话长点击处理
     */
    val onLongClickCommand = createTypeCommand<View> {
        this?.let {
            val popupSession = PopupSession(it.context, this@ItemViewModelSession)
            popupSession.setPopupGravity(Gravity.CENTER)
                .setBackgroundColor(android.R.color.transparent)
                .showPopupWindow()
        }
    }

    /**
     * 会话点击处理
     */
    val onClickCommand = createCommand {
        start(
            R.id.action_fragmentMain_to_navigation_message,
            arg = ArgMessage(contact.contactId, contact.sessionType.value)
        )
    }

    /**
     * 置顶
     */
    fun top() {
        val extension = contact.extension ?: mutableMapOf()
        extension[SESSION_ON_TOP] = System.currentTimeMillis()
        contact.extension = extension
        viewModel.msgService.updateRecent(contact)
        isTop.value = true
        viewModel.refresh()
    }

    /**
     * 取消置顶
     */
    fun unTop() {
        val extension = contact.extension ?: mutableMapOf()
        extension[SESSION_ON_TOP] = null
        contact.extension = extension
        viewModel.msgService.updateRecent(contact)
        isTop.value = false
        viewModel.refresh()
    }

    /**
     * 判断是否置顶
     */
    private fun isOnTop(): Boolean {
        val extension = contact.extension ?: return false
        extension[SESSION_ON_TOP] ?: return false
        return true
    }

    /**
     * 判断是否是通一条消息
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemViewModelSession

        if (contactId != other.contactId) return false

        return true
    }

    override fun hashCode(): Int {
        return contactId?.hashCode() ?: 0
    }


}