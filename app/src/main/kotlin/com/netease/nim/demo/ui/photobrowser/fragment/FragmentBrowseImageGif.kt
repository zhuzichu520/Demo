package com.netease.nim.demo.ui.photobrowser.fragment

import androidx.lifecycle.Observer
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentBrowseImageGifBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.photobrowser.arg.ArgBrowserGif
import com.netease.nim.demo.ui.photobrowser.viewmodel.ViewModelBrowseImageGif
import kotlinx.android.synthetic.main.fragment_browse_image_gif.*

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/14 11:45 AM
 * since: v 1.0.0
 */
class FragmentBrowseImageGif :
    FragmentBase<FragmentBrowseImageGifBinding, ViewModelBrowseImageGif, ArgBrowserGif>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_browse_image_gif

    override fun initView() {
        super.initOneData()

        photo.maximumScale = 4f
        photo.setZoomTransitionDuration(350)

        photo.setOnPhotoTapListener { _, _, _ ->
            back()
        }

        photo.setOnOutsidePhotoTapListener {
            back()
        }

    }

    override fun initViewObservable() {
        super.initViewObservable()

        /**
         * 消息状态监听
         */
        viewModel.toObservable(NimEvent.OnMessageStatusEvent::class.java, Observer {
            val message = it.message
            if (message.uuid == arg.message.uuid) {
                viewModel.updateView(it.message)
            }
        })

    }

    override fun initOneData() {
        super.initOneData()
        viewModel.updateView(arg.message)
    }


}