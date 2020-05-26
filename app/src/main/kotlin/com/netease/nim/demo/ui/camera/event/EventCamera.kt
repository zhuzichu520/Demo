package com.netease.nim.demo.ui.camera.event

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/18 10:20 AM
 * since: v 1.0.0
 */
class EventCamera {

    data class OnCameraEvent(
        var type: Int,
        var path: String,
        var cameraType: Int
    )

    companion object {
        //图片
        const val TYPE_IMAGE = 1
        //视频
        const val TYPE_VIDEO = 2
        //啥都不是
        const val TYPE_NULL = 3
    }

}