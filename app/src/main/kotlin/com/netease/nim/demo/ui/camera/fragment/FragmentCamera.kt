package com.netease.nim.demo.ui.camera.fragment

import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentCameraBinding
import com.netease.nim.demo.ui.camera.viewmodel.ViewModelCamera

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/9 9:23 PM
 * since: v 1.0.0
 */

class FragmentCamera : FragmentBase<FragmentCameraBinding, ViewModelCamera, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_camera

}