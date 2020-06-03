package com.netease.nim.demo.ui.main

import android.content.Context
import android.content.Intent
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ActivityBase
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.login.ActivityLogin

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/29 4:14 PM
 * since: v 1.0.0
 */
class ActivityMain : ActivityBase() {

    companion object {

        private const val TYPE = "TYPE"
        private const val TYPE_LOGOUT = 1

        fun logout(context: Context) {
            val intent = Intent(context, ActivityMain::class.java)
            intent.putExtra(TYPE, TYPE_LOGOUT)
            context.startActivity(intent)
        }
    }

    override fun setNavGraph(): Int = R.navigation.navigation_main

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when (intent?.getIntExtra(TYPE, -1)) {
            TYPE_LOGOUT -> {
                NimUserStorage.logout()
                startActivity(ActivityLogin::class.java, isPop = true)
            }
        }
    }

}