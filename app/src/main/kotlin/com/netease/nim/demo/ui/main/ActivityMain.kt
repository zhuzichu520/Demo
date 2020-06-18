package com.netease.nim.demo.ui.main

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hiwitech.android.libs.tool.jumpEmail
import com.hiwitech.android.libs.tool.toCast
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ActivityBase
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.avchat.ActivityAvchat
import com.netease.nim.demo.ui.avchat.arg.ArgAvchat
import com.netease.nim.demo.ui.login.ActivityLogin
import com.netease.nim.demo.ui.login.main.arg.ArgLogin
import com.netease.nimlib.sdk.avchat.model.AVChatData

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/29 4:14 PM
 * since: v 1.0.0
 */
class ActivityMain : ActivityBase() {

    companion object {

        private const val TYPE = "type"

        private const val TYPE_LOGOUT = 1
        private const val LOGOUT_KICKOUT = "logout_kickout"

        private const val TYPE_AVCHAT = 2
        private const val AVCHAT_DATA = "avchat_data"


        fun logout(context: Context, isKickOut: Boolean? = false) {
            val intent = Intent(context, ActivityMain::class.java)
            intent.putExtra(TYPE, TYPE_LOGOUT)
            intent.putExtra(LOGOUT_KICKOUT, isKickOut)
            context.startActivity(intent)
        }

        fun avchat(context: Context, data: AVChatData) {
            val intent = Intent(context, ActivityMain::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            intent.putExtra(TYPE, TYPE_AVCHAT)
            intent.putExtra(AVCHAT_DATA, data)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            pendingIntent.send()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent(intent)
    }

    override fun setNavGraph(): Int = R.navigation.navigation_main

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent == null)
            return
        parseIntent(intent)
    }

    private fun parseIntent(intent: Intent) {
        when (intent.getIntExtra(TYPE, -1)) {
            TYPE_LOGOUT -> {
                NimUserStorage.logout()
                startActivity(
                    ActivityLogin::class.java,
                    isPop = true,
                    arg = ArgLogin(intent.getBooleanExtra(LOGOUT_KICKOUT, false))
                )
            }
            TYPE_AVCHAT -> {
                val data: AVChatData = intent.getSerializableExtra(AVCHAT_DATA)?.toCast() ?: return
                startActivity(
                    ActivityAvchat::class.java,
                    arg = ArgAvchat(
                        ArgAvchat.TYPE_INCOMING,
                        data.account,
                        data.chatType,
                        data
                    )
                )
            }
        }
    }

}