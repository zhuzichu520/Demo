package com.netease.nim.demo.ui.session.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.nim.tools.ToolTeam
import com.netease.nim.demo.nim.tools.ToolUserInfo
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.zhuzichu.android.mvvm.base.BaseViewModel
import com.zhuzichu.android.shared.tools.ToolDate

class ItemViewModelSession(
    viewModel: BaseViewModel<*>,
    contact: RecentContact
) : ItemViewModelBase(viewModel) {

    private val contactId = contact.contactId

    val avatar = MutableLiveData<Any>()
    val name = MutableLiveData<String>()
    val error = MutableLiveData<Int>()
    val placeholder = MutableLiveData<Int>()
    val content = MutableLiveData<String>()
    val number = MutableLiveData<Int>()
    val date = MutableLiveData<String>()

    init {
        val contactId = contact.contactId
        content.value = contact.content
        number.value = contact.unreadCount
        date.value = ToolDate.getTimeShowString(contact.time, false)
        when (contact.sessionType) {
            SessionTypeEnum.P2P -> {
                val userInfo = ToolUserInfo.getUserInfo(contactId)
                name.value = userInfo.name
                avatar.value = userInfo.avatar
                error.value = R.mipmap.nim_avatar_default
                placeholder.value = R.mipmap.nim_avatar_default
            }
            SessionTypeEnum.Team -> {
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