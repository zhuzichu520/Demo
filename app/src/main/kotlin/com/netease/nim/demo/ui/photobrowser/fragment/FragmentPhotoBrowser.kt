package com.netease.nim.demo.ui.photobrowser.fragment

import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.SharedElementCallback
import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager
import com.hiwitech.android.mvvm.Mvvm.KEY_ARG
import com.hiwitech.android.shared.base.DefaultIntFragmentStatePagerAdapter
import com.hiwitech.android.shared.bus.LiveDataBus
import com.hiwitech.android.shared.ext.fitSystemWindows
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentPhotoBrowserBinding
import com.netease.nim.demo.ui.photobrowser.arg.ArgBrowser
import com.netease.nim.demo.ui.photobrowser.arg.ArgPhotoBrowser
import com.netease.nim.demo.ui.photobrowser.event.EventPhotoBrowser
import com.netease.nim.demo.ui.photobrowser.viewmodel.ViewModelPhotoBrowser
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import kotlinx.android.synthetic.main.fragment_browse_image.*
import kotlinx.android.synthetic.main.fragment_photo_browser.*

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/11 10:39 AM
 * since: v 1.0.0
 */
class FragmentPhotoBrowser :
    FragmentBase<FragmentPhotoBrowserBinding, ViewModelPhotoBrowser, ArgPhotoBrowser>() {

    companion object {
        const val TRANSITION_NAME = "PhotoBrowserShareView"
    }

    private lateinit var fragments: List<FragmentBase<*, *, *>>

    private lateinit var messageList: List<IMMessage>

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_photo_browser

    override fun initView() {
        super.initView()
        adjustInsets()
        var position = 0
        messageList = arg.messageList.reversed()
        fragments = messageList.mapIndexed { index, item ->
            if (arg.message.uuid == item.uuid) {
                position = index
            }
            if (item.msgType == MsgTypeEnum.image) {
                FragmentBrowseImage().apply {
                    arguments = bundleOf(KEY_ARG to ArgBrowser(item))
                }
            } else {
                FragmentBrowseVideo().apply {
                    arguments = bundleOf(KEY_ARG to ArgBrowser(item))
                }
            }
        }

        pager.adapter = DefaultIntFragmentStatePagerAdapter(childFragmentManager, fragments)
        pager.setCurrentItem(position, false)
        updateEnterSharedElement(position)
    }

    private fun adjustInsets() {
        pager.fitSystemWindows()
    }

    override fun initListener() {
        super.initListener()
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                updateEnterSharedElement(position)
            }

        })
    }

    private fun updateEnterSharedElement(index: Int) {
        ActivityCompat.setEnterSharedElementCallback(requireActivity(), object :
            SharedElementCallback() {
            override fun onSharedElementEnd(
                sharedElementNames: MutableList<String>,
                sharedElements: MutableList<View>,
                sharedElementSnapshots: MutableList<View>
            ) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
                view_background.animate().alpha(1f).start()
            }

            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                super.onMapSharedElements(names, sharedElements)
                sharedElements.clear()
                val fragment = fragments[index]
                if (fragment is FragmentBrowseImage) {
                    fragment.photo?.let { sharedElements.put(TRANSITION_NAME, it) }
                } else if (fragment is FragmentBrowseVideo) {
                    fragment.view_image?.let { sharedElements.put(TRANSITION_NAME, it) }
                }
            }
        })
        LiveDataBus.post(EventPhotoBrowser.OnUpdateEnterSharedElement(messageList[index]))
    }

    override fun onDestroyView() {
        view_background.animate().alpha(0f).start()
        super.onDestroyView()
    }
}