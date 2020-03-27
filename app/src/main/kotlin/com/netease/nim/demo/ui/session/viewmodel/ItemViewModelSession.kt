package com.netease.nim.demo.ui.session.viewmodel

import android.view.Gravity
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.nim.tools.ToolTeam
import com.netease.nim.demo.nim.tools.ToolUserInfo
import com.netease.nim.demo.ui.session.popup.PopupSession
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.zhuzichu.android.mvvm.base.BaseViewModel
import com.zhuzichu.android.shared.ext.createTypeCommand
import com.zhuzichu.android.shared.tools.ToolDate
import com.zhuzichu.android.widget.badge.QBadgeView

data class ItemViewModelSession(
    val viewModel: BaseViewModel<*>,
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

    val onLongClickCommand = createTypeCommand<View> {
        this?.let {
            //            val popup = PopupMenu(it.context, it, Gravity.CENTER_HORIZONTAL)
////            popup.menuInflater.inflate(R.menu.session, popup.menu)
////            popup.show()
//            val popup = popupMenu {
//                section {
//                    item {
//                        label = "置顶置顶置顶置顶置顶置顶置顶置顶置顶置顶置顶置顶置顶置顶置顶置顶"
//                        callback = {
//                            "置顶".toast()
//                        }
//                    }
//                }
//            }
//            popup.show(it.context, it)
            PopupSession(it.context).setPopupGravity(Gravity.CENTER)
                .setBackgroundColor(android.R.color.transparent).showPopupWindow()
        }
    }

    val initItemViewCommand = createTypeCommand<View> {
        this?.let {
            val badge = QBadgeView(it.context).bindTarget(it)
            badge.badgeGravity = Gravity.TOP or Gravity.END
            badge.badgeNumber = contact.unreadCount
            badge.setGravityOffset(16f, 30f, true)
            badge.setOnDragStateChangedListener { _, _, _ -> }
        }
    }

    init {
        val contactId = contact.contactId
        content.value = contact.content
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