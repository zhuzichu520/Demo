package com.netease.nim.demo.ui.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.common.base.Optional
import com.hiwitech.android.shared.ext.autoLoading
import com.hiwitech.android.shared.ext.map
import com.hiwitech.android.shared.ext.toStringByResId
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetUserInfo
import com.netease.nim.demo.ui.profile.viewmodel.ItemViewModelProfileEdit
import com.netease.nim.demo.ui.session.Constants.SESSION_ON_TOP
import com.netease.nim.demo.ui.user.arg.ArgUser
import com.netease.nim.demo.ui.user.domain.UseCaseGetFriendByAccount
import com.netease.nim.demo.ui.user.domain.UseCaseGetSession
import com.netease.nimlib.sdk.friend.FriendService
import com.netease.nimlib.sdk.friend.model.Friend
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import com.uber.autodispose.autoDispose
import io.reactivex.Flowable
import io.reactivex.functions.Function3
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject


/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 11:40 AM
 * since: v 1.0.0
 */
class ViewModelUser @Inject constructor(
    private val useCaseGetUserInfo: UseCaseGetUserInfo,
    private val useCaseGetFriendByAccount: UseCaseGetFriendByAccount,
    private val useCaseGetSession: UseCaseGetSession,
    private val friendService: FriendService

) : ViewModelBase<ArgUser>() {


    val items = MutableLiveData<List<Any>>()

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelUserHeader>(BR.item, R.layout.item_user_header)
        map<ItemViewModelUserSwitch>(BR.item, R.layout.item_user_switch)
        map<ItemViewModelProfileEdit>(BR.item, R.layout.item_profile_edit)
    }

    fun loadUserInfo() {
        val account = arg.account
        Flowable.zip(
            useCaseGetUserInfo.execute(account),
            useCaseGetFriendByAccount.execute(account),
            useCaseGetSession.execute(account),
            Function3<NimUserInfo, Friend, Optional<RecentContact>, List<Any>> { useInfo, friend, recent ->
                val alias = if (friend.alias.isNullOrEmpty())
                    R.string.no_setting.toStringByResId()
                else
                    friend.alias
                val isBlack = friendService.isInBlackList(account)
                val isNotice = friendService.isNeedMessageNotify(account)
                val isSticky = if (recent.isPresent) isSticky(recent.get()) else false
                listOf(
                    ItemViewModelUserHeader(this, useInfo),
                    ItemViewModelProfileEdit(this, R.string.alias, alias),
                    ItemViewModelUserSwitch(this, R.string.black_list, isBlack),
                    ItemViewModelUserSwitch(this, R.string.message_notice, isNotice),
                    ItemViewModelUserSwitch(this, R.string.message, isSticky)
                )
            }
        ).autoLoading(this).autoDispose(this).subscribe(
            { items.value = it },
            {
                handleThrowable(it)
            }
        )

    }

    /**
     * 判断是否置顶
     * @param contact 最近会话
     */
    private fun isSticky(contact: RecentContact): Boolean {
        val extension = contact.extension ?: return false
        extension[SESSION_ON_TOP] ?: return false
        return true
    }

}