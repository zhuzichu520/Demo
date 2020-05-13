package com.netease.nim.demo.ui.photobrowser

import android.os.Bundle
import com.hiwitech.android.libs.tool.immersiveDark
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ActivityBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/11 10:39 AM
 * since: v 1.0.0
 */
class ActivityPhotoBrowser : ActivityBase() {

    override fun setNavGraph(): Int = R.navigation.navigation_photo_browser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersiveDark(darkMode = false)
    }

}