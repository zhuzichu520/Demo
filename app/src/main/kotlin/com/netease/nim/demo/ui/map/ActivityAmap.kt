package com.netease.nim.demo.ui.map

import android.os.Bundle
import com.hiwitech.android.libs.tool.immersiveDark
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ActivityBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/8 3:19 PM
 * since: v 1.0.0
 */
class ActivityAmap : ActivityBase() {
    override fun setNavGraph(): Int = R.navigation.navigation_amap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersiveDark(darkMode = false)
    }

}