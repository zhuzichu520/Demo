package com.netease.nim.demo.ui.camera.event

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/18 10:20 AM
 * since: v 1.0.0
 */
class EventCamera {
    data class OnCameraImageEvent(
        val path: String
    )

    data class OnCameraVideoEvent(
        val path: String
    )

}