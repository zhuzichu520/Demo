package com.netease.nim.demo.ui.photobrowser.fragment

import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentBrowseVideoBinding
import com.netease.nim.demo.ui.photobrowser.arg.ArgBrowser
import com.netease.nim.demo.ui.photobrowser.viewmodel.ViewModelBrowseVideo

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/12 2:51 PM
 * since: v 1.0.0
 */
class FragmentBrowseVideo :
    FragmentBase<FragmentBrowseVideoBinding, ViewModelBrowseVideo, ArgBrowser>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_browse_video

}