package com.netease.nim.demo.ui.session.viewmodel

import android.view.Gravity
import android.view.View
import androidx.lifecycle.MutableLiveData
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
import com.zhuzichu.android.shared.ext.createCommand
import com.zhuzichu.android.shared.ext.createTypeCommand
import com.zhuzichu.android.shared.tools.ToolDate
import com.zhuzichu.android.widget.badge.Badge

data class ItemViewModelSession(
    val viewModel: ViewModelSession,
    val contact: RecentContact
) : ItemViewModelBase(viewModel) {

    private val contactId = contact.contactId
    val time = contact.time

    val avatar = MutableLiveData<Any>()
    val name = MutableLiveData<String>()
    val error = MutableLiveData<Int>()
    val placeholder = MutableLiveData<Int>()
    val content = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val isTop = MutableLiveData<Boolean>()
    val number = MutableLiveData<Int>()

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

    val onDragStateChangedCommamnd = createTypeCommand<Int> {
        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == this) {
            viewModel.msgService.clearUnreadCount(contactId, contact.sessionType)
        }
    }

    val onLongClickCommand = createTypeCommand<View> {
        this?.let {
            val popupSession = PopupSession(it.context, this@ItemViewModelSession)
            popupSession.setPopupGravity(Gravity.CENTER)
                .setBackgroundColor(android.R.color.transparent)
                .showPopupWindow()
        }
    }

    val onClickCommand = createCommand {
        start(
            R.id.action_fragmentMain_to_navigation_message,
            arg = ArgMessage(contact.contactId, contact.sessionType.value)
        )
    }

    fun top() {
        val extension = contact.extension ?: mutableMapOf()
        extension[SESSION_ON_TOP] = System.currentTimeMillis()
        contact.extension = extension
        viewModel.msgService.updateRecent(contact)
        isTop.value = true
        viewModel.refresh()
    }

    fun unTop() {
        val extension = contact.extension ?: mutableMapOf()
        extension[SESSION_ON_TOP] = null
        contact.extension = extension
        viewModel.msgService.updateRecent(contact)
        isTop.value = false
        viewModel.refresh()
    }

    private fun isOnTop(): Boolean {
        val extension = contact.extension ?: return false
        extension[SESSION_ON_TOP] ?: return false
        return true
    }

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