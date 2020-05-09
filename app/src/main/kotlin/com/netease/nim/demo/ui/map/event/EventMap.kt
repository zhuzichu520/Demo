package com.netease.nim.demo.ui.map.event

import com.netease.nim.demo.ui.map.viewmodel.ItemViewModelPoi

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/9 11:17 AM
 * since: v 1.0.0
 */
class EventMap {
    data class OnSendLocationEvent(
        val itemViewModelPoi: ItemViewModelPoi
    )
}