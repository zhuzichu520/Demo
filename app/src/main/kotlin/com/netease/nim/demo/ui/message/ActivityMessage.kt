package com.netease.nim.demo.ui.message

import android.os.Bundle
import com.hiwitech.android.shared.bus.LiveDataBus
import com.hiwitech.android.shared.ext.logi
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ActivityBase
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.tools.ToolKeyboard
import com.netease.nim.demo.ui.launcher.event.OnKeyboardChangeEvent

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/14 9:49 AM
 * since: v 1.0.0
 */
class ActivityMessage : ActivityBase() {

    override fun setNavGraph(): Int = R.navigation.navigation_message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registKeyboardListener()
    }

    private fun registKeyboardListener() {
        ToolKeyboard(
            this,
            onKeyboardShow = {
                NimUserStorage.softKeyboardHeight = this
                ("onKeyboardShow:$this").logi("StatusBar")
            },
            onKeyboardChange = {
                NimUserStorage.softKeyboardHeight = this
                ("onKeyboardChange:$this").logi("StatusBar")
                LiveDataBus.post(OnKeyboardChangeEvent())
            },
            onKeyboardHide = {

            }
        )
    }

}