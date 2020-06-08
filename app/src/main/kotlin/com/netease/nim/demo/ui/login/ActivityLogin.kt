package com.netease.nim.demo.ui.login

import android.os.Bundle
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ActivityBase
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.tools.ToolKeyboard

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/29 4:16 PM
 * since: v 1.0.0
 */
class ActivityLogin : ActivityBase() {

    override fun setNavGraph(): Int = R.navigation.navigation_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registKeyboardListener()
    }

    private fun registKeyboardListener() {
        ToolKeyboard(
            this,
            onKeyboardShow = {
                NimUserStorage.softKeyboardHeight = this
            },
            onKeyboardChange = {
                NimUserStorage.softKeyboardHeight = this
            },
            onKeyboardHide = {

            }
        )
    }

}