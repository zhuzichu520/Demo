package com.netease.nim.demo.base

import android.os.Bundle
import com.hiwitech.android.mvvm.base.BaseActivity
import com.hiwitech.android.shared.theme.ThemeManager

/**
 * desc Activity基类
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
abstract class ActivityBase : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.applyThemeOverlays(this)
        super.onCreate(savedInstanceState)
    }
}