package com.netease.nim.demo.helper

import android.app.PendingIntent
import android.content.ContentResolver
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.hiwitech.android.shared.ext.toUri
import com.hiwitech.android.shared.global.AppGlobal.context
import com.hiwitech.android.widget.notify.Notify
import com.netease.nim.demo.R

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/18 2:32 PM
 * since: v 1.0.0
 */
class HelperNotify {

    companion object {
        private var idAvchat = 0x001
        const val NOTIFY_KEY = "DEMO"
    }


    fun notifyAvChat(isShown: Boolean, intent: PendingIntent? = null) {
        if (isShown) {
            idAvchat = Notify.with(context).meta {
                sticky = true
                this.clickIntent = intent
            }.alerting(NOTIFY_KEY) {
                sound = R.raw.avchat_ring.toUri()
                channelImportance = Notify.IMPORTANCE_MAX
                channelName = "音视频通话邀请通知"
                vibrationPattern = arrayListOf(1000L, 500L, 1000L)
                channelDescription = "云信Demo收到音视屏通话邀请的通知类型"
                lockScreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }.content {
                title = "有人呼叫你"
                text = "有人呼叫你有人呼叫你有人呼叫你!"
            }.show()
        } else {
            Notify.cancelNotification(context, idAvchat)
        }
    }

}