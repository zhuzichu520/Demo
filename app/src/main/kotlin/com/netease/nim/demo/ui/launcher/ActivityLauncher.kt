package com.netease.nim.demo.ui.launcher

import android.os.Bundle
import com.hiwitech.android.shared.bus.LiveDataBus
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.hiwitech.android.shared.ext.createFlowable
import com.hiwitech.android.shared.ext.logi
import com.hiwitech.android.shared.ext.toast
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ActivityBase
import com.netease.nim.demo.nim.emoji.EmojiManager
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.tools.ToolKeyboard
import com.netease.nim.demo.ui.launcher.event.OnKeyboardChangeEvent
import com.uber.autodispose.android.lifecycle.autoDispose


/**
 * desc 启动Activity
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ActivityLauncher : ActivityBase() {

    override fun setNavGraph(): Int = R.navigation.navigation_launcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registKeyboardListener()
        initSchedulers()
    }

    /**
     * 异步初始化
     */
    private fun initSchedulers() {
        createFlowable<Long> {
            val start = System.currentTimeMillis()
            EmojiManager.initLoad(applicationContext)
            val end = System.currentTimeMillis()
            onNext(end - start)
            onComplete()
        }.bindToSchedulers()
            .autoDispose(this)
            .subscribe {
                "初始化总耗时:".plus(it).toast()
            }
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