package com.netease.nim.demo.nim.event

import android.app.ActivityManager
import android.content.Context
import com.hiwitech.android.shared.bus.LiveDataBus
import com.netease.nim.demo.base.IBus
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.AuthServiceObserver
import com.netease.nimlib.sdk.auth.OnlineClient
import com.netease.nimlib.sdk.avchat.AVChatManager
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent
import com.netease.nimlib.sdk.avchat.model.AVChatControlEvent
import com.netease.nimlib.sdk.avchat.model.AVChatData
import com.netease.nimlib.sdk.friend.FriendServiceObserve
import com.netease.nimlib.sdk.friend.model.FriendChangedNotify
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.msg.model.AttachmentProgress
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.uinfo.UserServiceObserve
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer as XObserver
import com.hiwitech.android.libs.tool.isAppOnForeground
import com.hiwitech.android.mvvm.Mvvm.KEY_ARG
import com.hiwitech.android.shared.databinding.imageview.bindImageViewGlide
import com.hiwitech.android.shared.ext.toast
import com.hiwitech.android.widget.notify.Notify
import com.hiwitech.android.widget.qmui.QMUIRadiusImageView
import com.jakewharton.rxbinding3.view.clicks
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.interfaces.OnInvokeView
import com.lzf.easyfloat.permission.PermissionUtils
import com.netease.nim.demo.R
import com.netease.nim.demo.manager.ManagerActivity
import com.netease.nim.demo.nim.repository.NimRepositoryImpl
import com.netease.nim.demo.nim.tools.ToolUserInfo
import com.netease.nim.demo.ui.avchat.ActivityAvchat
import com.netease.nim.demo.ui.avchat.arg.ArgAvchat
import com.netease.nim.demo.ui.avchat.domain.UseCaseHangUp
import com.netease.nim.demo.ui.main.ActivityMain
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetUserInfo
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.avchat.constant.AVChatType
import com.uber.autodispose.android.autoDispose

object NimEventManager : IBus {

    private lateinit var context: Context

    private var useCaseHangUp: UseCaseHangUp = UseCaseHangUp(NimRepositoryImpl())

    private var notifyId = -1

    override val observers: HashMap<Class<Any>, XObserver<Any>> get() = hashMapOf()

    fun init(context: Context) {
        this.context = context
        registerObserves(true)
        initViewObservable()
    }

    private fun initViewObservable() {
        toObservable(NimEvent.OnInComingCallEvent::class.java, XObserver {
            if (context.isAppOnForeground()) {
                startChatActivity(it.data)
            } else {
                if (PermissionUtils.checkPermission(context)) {
                    showFloatChat(true, it.data)
                } else {
                    showNotifyChat(true)
                }
            }
        })

        toObservable(NimEvent.OnHangUpNotificationEvent::class.java, XObserver {
            showNotifyChat(false)
            showFloatChat(false)
        })
    }

    private fun startChatActivity(data: AVChatData) {
        val intent = Intent(context, ActivityAvchat::class.java)
        intent.putExtras(
            bundleOf(
                KEY_ARG to ArgAvchat(
                    ArgAvchat.TYPE_INCOMING,
                    data.account,
                    data.chatType,
                    data
                )
            )
        )
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }


    private fun showFloatChat(isShown: Boolean, data: AVChatData? = null) {
        if (isShown) {
            EasyFloat.with(context)
                .setMatchParent(widthMatch = true, heightMatch = false)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setGravity(Gravity.TOP)
                .setLayout(R.layout.layout_float_chat, OnInvokeView {
                    updateChatView(it, data!!)
                })
                .show()
        } else {
            EasyFloat.dismissAppFloat()
        }
    }

    private fun updateChatView(
        view: View,
        data: AVChatData
    ) {
        val root = view.findViewById<View>(R.id.root)
        val avatar = view.findViewById<QMUIRadiusImageView>(R.id.avatar)
        val name = view.findViewById<TextView>(R.id.name)
        val close = view.findViewById<ImageView>(R.id.close)
        val call = view.findViewById<ImageView>(R.id.call)
        val content = view.findViewById<TextView>(R.id.content)
        name.text = ToolUserInfo.getUserDisplayName(data.account)
        when (data.chatType) {
            AVChatType.AUDIO -> {
                content.setText(R.string.avchat_audio_call_request)
            }
            AVChatType.VIDEO -> {
                content.setText(R.string.avchat_video_call_request)
            }
            else -> {
            }
        }
        bindImageViewGlide(
            avatar,
            ToolUserInfo.getUserInfo(data.account).avatar,
            R.mipmap.nim_avatar_default,
            R.mipmap.nim_avatar_default
        )

        root.clicks().autoDispose(view).subscribe {
            val context = ManagerActivity.INST.getTopActivity() ?: this.context
            ActivityMain.avchat(context, data)
        }

        call.clicks().autoDispose(view).subscribe {

        }

        close.clicks().autoDispose(view).subscribe {
            useCaseHangUp.execute(data.chatId).autoDispose(view).subscribe {
                EasyFloat.dismissAppFloat()
            }
        }
    }

    private fun showNotifyChat(isShown: Boolean) {
        if (isShown) {
            notifyId = Notify.with(context).meta {
                this.sticky = true
            }.alerting("key") {
                lockScreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }.content {
                title = "有人呼叫你"
                text = "有人呼叫你有人呼叫你有人呼叫你!"
            }.show()
        } else {
            Notify.cancelNotification(context, notifyId)
        }
    }


    /**
     * 联系人监听
     */
    private val observerRecentContact =
        Observer { list: List<RecentContact>? ->
            if (!list.isNullOrEmpty()) {
                LiveDataBus.post(NimEvent.OnRecentContactEvent(list))
            }
        }

    /**
     * 用户在线监听
     */
    private val observeOnlineStatus =
        Observer { statusCode: StatusCode? ->
            statusCode?.let {
                LiveDataBus.post(NimEvent.OnLineStatusEvent(it))
            }
        }

    /**
     * 多端登录监听
     */
    private val observeOnlineClient =
        Observer { onlineClients: List<OnlineClient>? ->
            LiveDataBus.post(NimEvent.OnLienClientEvent(onlineClients))
        }

    /**
     * 消息状态监听
     */
    private val observeMessageStatus =
        Observer { message: IMMessage? ->
            message?.let {
                LiveDataBus.post(NimEvent.OnMessageStatusEvent(it))
            }
        }

    /**
     * 消息接受监听
     */
    private val observeReceiveMessage =
        Observer { list: List<IMMessage>? ->
            if (!list.isNullOrEmpty()) {
                LiveDataBus.post(NimEvent.OnReceiveMessageEvent(list))
            }
        }

    /**
     * 附件下载监听
     */
    private val observerAttachmentProgress =
        Observer { attachmentProgress: AttachmentProgress? ->
            attachmentProgress?.let {
                LiveDataBus.post(NimEvent.OnAttachmentProgressEvent(it))
            }
        }

    /**
     * 监听用户资料变更  不会实时监听
     * 用户资料除自己之外，不保证其他用户资料实时更新。其他用户数据更新时机为：
     * 1. 调用 fetchUserInfo 方法刷新用户
     * 2. 收到此用户发来消息（如果消息发送者有资料变更，SDK 会负责更新并通知）
     * 3. 程序再次启动，此时会同步好友信息
     */
    private val observerUserInfoUpdate =
        Observer { list: List<NimUserInfo>? ->
            if (!list.isNullOrEmpty()) {
                LiveDataBus.post(NimEvent.OnUserInfoUpdateEvent(list))
            }
        }

    /**
     * 第三方 APP 应在 APP 启动后监听好友关系的变化，当主动添加好友成功、被添加为好友、
     * 主动删除好友成功、被对方解好友关系、好友关系更新、登录同步好友关系数据时都会收到通知。
     */
    private val observerFriendChangedNotify =
        Observer { friendChangedNotify: FriendChangedNotify ->
            val addedOrUpdatedFriends = friendChangedNotify.addedOrUpdatedFriends
            val deletedFriends = friendChangedNotify.deletedFriends
            if (!addedOrUpdatedFriends.isNullOrEmpty()) {
                LiveDataBus.post(NimEvent.OnAddedOrUpdatedFriendsEvent(addedOrUpdatedFriends))
            }
            if (!deletedFriends.isNullOrEmpty()) {
                LiveDataBus.post(NimEvent.OnDeletedFriendsEvent(deletedFriends))
            }
        } as Observer<FriendChangedNotify>

    /**
     * 收到对方蒂娜花
     */
    private val observeInComingCall =
        Observer { data: AVChatData ->
            LiveDataBus.post(NimEvent.OnInComingCallEvent(data))
        }

    /**
     * 通话过程中，收到对方挂断电话
     */
    private val observeHangUpNotification =
        Observer { event: AVChatCommonEvent ->
            LiveDataBus.post(NimEvent.OnHangUpNotificationEvent(event))
        }

    private val observeCalleeAckNotification =
        Observer { event: AVChatCalleeAckEvent ->
            LiveDataBus.post(NimEvent.OnCalleeAckNotificationEvent(event))
        }

    private val observeControlNotification =
        Observer { event: AVChatControlEvent ->
            LiveDataBus.post(NimEvent.OnControlNotificationEvent(event))
        }

    fun registerObserves(register: Boolean) {

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeAttachmentProgress(observerAttachmentProgress, register)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeReceiveMessage(observeReceiveMessage, register)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeMsgStatus(observeMessageStatus, register)

        NIMClient.getService(MsgServiceObserve::class.java)
            .observeRecentContact(observerRecentContact, register)

        NIMClient.getService(AuthServiceObserver::class.java)
            .observeOnlineStatus(observeOnlineStatus, register)

        NIMClient.getService(AuthServiceObserver::class.java)
            .observeOtherClients(observeOnlineClient, register)

        NIMClient.getService(UserServiceObserve::class.java)
            .observeUserInfoUpdate(observerUserInfoUpdate, register)

        NIMClient.getService(FriendServiceObserve::class.java)
            .observeFriendChangedNotify(observerFriendChangedNotify, register)

        AVChatManager.getInstance().observeIncomingCall(observeInComingCall, register)

        AVChatManager.getInstance().observeHangUpNotification(observeHangUpNotification, register)

        AVChatManager.getInstance()
            .observeCalleeAckNotification(observeCalleeAckNotification, register)

        AVChatManager.getInstance().observeControlNotification(observeControlNotification, register)

    }

}