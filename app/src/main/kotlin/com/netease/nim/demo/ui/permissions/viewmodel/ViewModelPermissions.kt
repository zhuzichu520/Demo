package com.netease.nim.demo.ui.permissions.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.base.ViewModelBase
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/7 5:55 PM
 * since: v 1.0.0
 */
class ViewModelPermissions @Inject constructor(
) : ViewModelBase<ArgDefault>() {

    val content = MutableLiveData<String>()

}