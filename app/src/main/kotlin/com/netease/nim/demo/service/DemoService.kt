package com.netease.nim.demo.service

import android.app.Service
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import com.hiwitech.android.libs.tool.isAppOnForeground
import com.hiwitech.android.mvvm.Mvvm.KEY_ARG
import com.hiwitech.android.shared.base.OkBinder
import com.hiwitech.android.shared.ext.logi
import com.hiwitech.android.widget.notify.Notify
import com.netease.nim.demo.base.IBus
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.nim.event.NimEventManager
import com.netease.nim.demo.ui.avchat.ActivityAvchat
import com.netease.nim.demo.ui.avchat.arg.ArgAvchat
import com.netease.nimlib.sdk.avchat.model.AVChatData


/**
 * desc
 * author: 朱子楚
 * time: 2020/6/16 8:32 PM
 * since: v 1.0.0
 */
class DemoService : Service(){

    companion object {
        const val TAG = "DemoService"
    }


    private val okBinder = OkBinder(object : IDemoService {
        override fun stopService() {
            stopSelf()
        }
    })

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        "onStartCommand".logi(TAG)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        "onCreate".logi(TAG)
    }


    override fun onBind(intent: Intent?): IBinder? {
        "onBind".logi(TAG)
        return okBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        "onUnbind".logi(TAG)
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        "onDestroy".logi(TAG)
    }

}