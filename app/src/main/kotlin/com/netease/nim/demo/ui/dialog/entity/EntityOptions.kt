package com.netease.nim.demo.ui.dialog.entity

import androidx.annotation.StringRes
import com.hiwitech.android.mvvm.event.SingleLiveEvent

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/26 11:07 AM
 * since: v 1.0.0
 */
data class EntityOptions(
    val option: Int,
    @StringRes val title: Int,
    val onClickItemEvent: SingleLiveEvent<EntityOptions>
)