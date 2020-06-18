package com.netease.nim.demo.ui.avchat.fragment

import androidx.activity.addCallback
import androidx.lifecycle.Observer
import com.netease.nim.demo.R
import com.netease.nim.demo.BR
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentAvchatVideoBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.avchat.arg.ArgAvchat
import com.netease.nim.demo.ui.avchat.viewmodel.ViewModelAvchatVideo

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/15 8:26 PM
 * since: v 1.0.0
 */
class FragmentAvchatVideo :
    FragmentBase<FragmentAvchatVideoBinding, ViewModelAvchatVideo, ArgAvchat>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_avchat_video

    override fun initView() {
        super.initView()
        initBackListener()
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.toObservable(NimEvent.OnHangUpNotificationEvent::class.java, Observer {
            finish()
        })
    }

    private fun initBackListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }

}