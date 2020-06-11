package com.netease.nim.demo.ui.user.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.common.base.Optional
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.autoLoading
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.map
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.nim.tools.ToolSticky
import com.netease.nim.demo.ui.message.ActivityMessage
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetUserInfo
import com.netease.nim.demo.ui.profile.viewmodel.ItemViewModelProfileEdit
import com.netease.nim.demo.ui.user.arg.ArgUser
import com.netease.nim.demo.ui.user.domain.*
import com.netease.nimlib.sdk.friend.FriendService
import com.netease.nimlib.sdk.friend.constant.FriendFieldEnum
import com.netease.nimlib.sdk.friend.model.Friend
import com.netease.nimlib.sdk.msg.MsgService
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
    private val useCaseAddFriend: UseCaseAddFriend,
    private val useCaseRemoveFromBlackList: UseCaseRemoveFromBlackList,
    private val useCaseAddToBlackList: UseCaseAddToBlackList,
    private val useCaseSetMessageNotify: UseCaseSetMessageNotify,
    private val useCaseUpdateFriendFields: UseCaseUpdateFriendFields,
    private val msgService: MsgService
) : ViewModelBase<ArgUser>() {

    val onClickDeleteEvent = SingleLiveEvent<Unit>()

    val onClickAliasEvent = SingleLiveEvent<ItemViewModelProfileEdit>()

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
            Function3<Optional<NimUserInfo>, Optional<Friend>, Optional<RecentContact>, List<Any>> { useInfo, friend, recent ->
                handleDate(account, useInfo, friend, recent)
            }
        ).autoLoading(this).autoDispose(this).subscribe(
            { items.value = it },
            {
                handleThrowable(it)
            }
        )

    }

    private fun handleDate(
        account: String,
        useInfo: Optional<NimUserInfo>,
        friend: Optional<Friend>,
        recent: Optional<RecentContact>
    ): List<Any> {
        val data = arrayListOf<Any>()
        data.add(ItemViewModelUserHeader(this, useInfo.get()))
        //是不是我的好友
        val isMyFriend = friendService.isMyFriend(arg.account)
        if (isMyFriend) {
            data.add(
                ItemViewModelProfileEdit(
                    this,
                    R.string.alias,
                    friend.get().alias,
                    onClickAliasEvent
                )
            )
        }
        //是否黑名单
        val isBlack = friendService.isInBlackList(account)
        data.add(
            ItemViewModelUserSwitch(this, R.string.black_list, isBlack) {
                changeBlack(this)
            }
        )
        //是否免打扰
        val isNotice = friendService.isNeedMessageNotify(account)
        data.add(
            ItemViewModelUserSwitch(this, R.string.message_notice, isNotice) {
                changeNotice(this)
            }
        )
        //是否置顶
        val isSticky = if (recent.isPresent) ToolSticky.isStickyTagSet(recent.get()) else false
        data.add(
            ItemViewModelUserSwitch(this, R.string.message_sticky, isSticky) {
                changeSticky(this, recent)
            }
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
        return data
    }

    /**
     * 改变置顶状态
     */
    private fun changeSticky(
        itemViewModelUserSwitch: ItemViewModelUserSwitch,
        recent: Optional<RecentContact>
    ) {
        val checked = !(itemViewModelUserSwitch.checked.value ?: false)
        val contact = if (recent.isPresent) {
            recent.get()
        } else {
            msgService.createEmptyRecentContact(
                arg.account,
                SessionTypeEnum.P2P,
                0L,
                System.currentTimeMillis(),
                true
            )
        }
        if (checked) {
            addStickyTag(contact)
        } else {
            removeStickTag(contact)
        }
        itemViewModelUserSwitch.checked.value = checked
    }

    /**
     * 改变免打扰状态
     */
    private fun changeNotice(itemViewModelUserSwitch: ItemViewModelUserSwitch) {
        val checked = !(itemViewModelUserSwitch.checked.value ?: false)
        useCaseSetMessageNotify.execute(UseCaseSetMessageNotify.Parameters(arg.account, checked))
            .autoDispose(this)
            .subscribe {
                itemViewModelUserSwitch.checked.value = checked
            }
    }

    /**
     * 改变黑名单状态
     */
    private fun changeBlack(itemViewModelUserSwitch: ItemViewModelUserSwitch) {
        val checked = !(itemViewModelUserSwitch.checked.value ?: false)
        if (checked) {
            useCaseAddToBlackList.execute(arg.account)
                .autoDispose(this)
                .subscribe {
                    itemViewModelUserSwitch.checked.value = checked
                }
        } else {
            useCaseRemoveFromBlackList.execute(arg.account)
                .autoDispose(this)
                .subscribe {
                    itemViewModelUserSwitch.checked.value = checked
                }
        }
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

    /**
     * 置顶
     */
    private fun addStickyTag(contact: RecentContact) {
        ToolSticky.addStickyTag(contact)
        msgService.updateRecentAndNotify(contact)
    }

    /**
     * 取消置顶
     */
    private fun removeStickTag(contact: RecentContact) {
        ToolSticky.removeStickTag(contact)
        msgService.updateRecentAndNotify(contact)
    }


    /**
     * 修改备注名
     */
    fun updateAlias(alias: String, success: (String) -> Unit) {
        useCaseUpdateFriendFields.execute(
            UseCaseUpdateFriendFields.Parameters(
                arg.account,
                mapOf(
                    FriendFieldEnum.ALIAS to alias
                )
            )
        ).autoLoading(this).autoDispose(this).subscribe {
            success.invoke(alias)
        }
    }

}