package com.netease.nim.demo.ui.session.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.nim.tools.ToolTeam
import com.netease.nim.demo.nim.tools.ToolUserInfo
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.zhuzichu.android.mvvm.base.BaseViewModel

class ItemViewModelSession(
    viewModel: BaseViewModel<*>,
    contact: RecentContact
) : ItemViewModelBase(viewModel) {

    val avatar = MutableLiveData<Any>()
    val name = MutableLiveData<String>()

    init {
        val contactId = contact.contactId
        when (contact.sessionType) {
            SessionTypeEnum.P2P -> {
                val userInfo = ToolUserInfo.getUserInfo(contactId)
                name.value = userInfo.name
                avatar.value =
                    if (userInfo.avatar.isNullOrEmpty())
                        R.mipmap.nim_avatar_group
                    else
                        userInfo.avatar
            }
            SessionTypeEnum.Team -> {
                val team = ToolTeam.getTeam(contactId)
                name.value = team.name
                avatar.value =
                    if (team.icon.isNullOrEmpty())
                        R.mipmap.nim_avatar_group
                    else
                        team.icon
            }
            else -> {

            }
        }
    }
}