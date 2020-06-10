package com.netease.nim.demo.ui.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.common.base.Optional
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.autoLoading
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.map
import com.hiwitech.android.shared.ext.toStringByResId
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.message.ActivityMessage
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetUserInfo
import com.netease.nim.demo.ui.profile.viewmodel.ItemViewModelProfileEdit
import com.netease.nim.demo.ui.session.Constants.SESSION_ON_TOP
import com.netease.nim.demo.ui.user.arg.ArgUser
import com.netease.nim.demo.ui.user.domain.UseCaseAddFriend
import com.netease.nim.demo.ui.user.domain.UseCaseDeleteFriend
import com.netease.nim.demo.ui.user.domain.UseCaseGetFriendByAccount
import com.netease.nim.demo.ui.user.domain.UseCaseGetSession
import com.netease.nimlib.sdk.friend.FriendService
import com.netease.nimlib.sdk.friend.model.Friend
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
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
    private val friendService: FriendService,
    private val useCaseDeleteFriend: UseCaseDeleteFriend,
    private val useCaseAddFriend: UseCaseAddFriend
) : ViewModelBase<ArgUser>() {

    val onClickDeleteEvent = SingleLiveEvent<Unit>()

    val items = MutableLiveData<List<Any>>()

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelUserHeader>(BR.item, R.layout.item_user_header)
        map<ItemViewModelUserSwitch>(BR.item, R.layout.item_user_switch)
        map<ItemViewModelProfileEdit>(BR.item, R.layout.item_profile_edit)
        map<ItemViewModelUserBottom>(BR.item, R.layout.item_user_bottoom)
    }

    fun updateUserInfo() {
        val account = arg.account
        Flowable.zip(
            useCaseGetUserInfo.execute(account),
            useCaseGetFriendByAccount.execute(account),
            useCaseGetSession.execute(account),
            Function3<NimUserInfo, Optional<Friend>, Optional<RecentContact>, List<Any>> { useInfo, friend, recent ->
                val data = arrayListOf<Any>()
                data.add(ItemViewModelUserHeader(this, useInfo))
                val isMyFriend = friendService.isMyFriend(arg.account)
                if (isMyFriend) {
                    //是好友，显示
                    val alias = if (friend.get().alias.isNullOrEmpty())
                        R.string.no_setting.toStringByResId()
                    else
                        friend.get().alias
                    data.add(ItemViewModelProfileEdit(this, R.string.alias, alias))

                }
                val isBlack = friendService.isInBlackList(account)
                data.add(ItemViewModelUserSwitch(this, R.string.black_list, isBlack))
                val isNotice = friendService.isNeedMessageNotify(account)
                data.add(
                    ItemViewModelUserSwitch(this, R.string.message_notice, isNotice)
                )
                val isSticky = if (recent.isPresent) isSticky(recent.get()) else false
                data.add(
                    ItemViewModelUserSwitch(this, R.string.message_sticky, isSticky)
                )
                data.add(
                    ItemViewModelUserBottom(this,
                        onClickChatCommand = createCommand {
                            startActivity(
                                ActivityMessage::class.java,
                                arg = ArgMessage(account, SessionTypeEnum.P2P.value)
                            )
                        },
                        onClickDeleteCommand = createCommand {
                            onClickDeleteEvent.call()
                        },
                        onClickAddCommand = createCommand {
                            addFriend()
                        }).apply {
                        if (isMyFriend) {
                            showDelete()
                        } else {
                            showAdd()
                        }
                    }
                )
                data
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

    /**
     * 删除好友
     */
    fun deleteFriend() {
        useCaseDeleteFriend.execute(arg.account)
            .autoLoading(this)
            .autoDispose(this)
            .subscribe {

            }
    }

    /**
     * 添加好友
     */
    private fun addFriend() {
        useCaseAddFriend.execute(arg.account)
            .autoLoading(this)
            .autoDispose(this)
            .subscribe {

            }
    }

}