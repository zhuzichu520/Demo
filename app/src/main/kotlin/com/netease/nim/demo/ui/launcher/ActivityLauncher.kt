package com.netease.nim.demo.ui.launcher

import com.netease.nim.demo.R
import com.netease.nim.demo.base.ActivityBase


/**
 * desc 启动Activity
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ActivityLauncher : ActivityBase() {

    override fun setNavGraph(): Int = R.navigation.navigation_launcher

}