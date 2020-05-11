package com.netease.nim.demo.ui.photobrowser.fragment

import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentPhotoBrowserBinding
import com.netease.nim.demo.ui.message.main.arg.ArgPhotoBrowser
import com.netease.nim.demo.ui.photobrowser.viewmodel.ViewModelPhotoBrowser

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/11 10:39 AM
 * since: v 1.0.0
 */
class FragmentPhotoBrowser :
    FragmentBase<FragmentPhotoBrowserBinding, ViewModelPhotoBrowser, ArgPhotoBrowser>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_photo_browser

}