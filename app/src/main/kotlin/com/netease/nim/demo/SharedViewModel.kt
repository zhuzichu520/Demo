package com.netease.nim.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * desc 整个activity共享ViewModel 用于 存放共享的数据
 * author: 朱子楚
 * time: 2020/4/21 3:24 PM
 * since: v 1.0.0
 */
class SharedViewModel : ViewModel() {
    //消息未读书变化
    val onSessionNumberChangeEvent = MutableLiveData<Int>()
}