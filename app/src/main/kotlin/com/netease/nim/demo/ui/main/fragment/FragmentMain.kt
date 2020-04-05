package com.netease.nim.demo.ui.main.fragment

import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.mvvm.base.BaseFragment
import com.hiwitech.android.shared.base.DefaultIntFragmentPagerAdapter
import com.hiwitech.android.shared.ext.plusBadge
import com.hiwitech.android.shared.ext.setupWithViewPager
import com.hiwitech.android.shared.ext.toast
import com.hiwitech.android.widget.badge.Badge
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.databinding.FragmentMainBinding
import com.netease.nim.demo.nim.event.NimEventManager
import com.netease.nim.demo.ui.contact.fragment.FragmentContact
import com.netease.nim.demo.ui.main.viewmodel.ViewModelMain
import com.netease.nim.demo.ui.me.main.fragment.FragmentMe
import com.netease.nim.demo.ui.session.fragment.FragmentSession
import com.netease.nimlib.sdk.msg.MsgService
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

/**
 * desc 主Fragment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentMain : BaseFragment<FragmentMainBinding, ViewModelMain, ArgDefault>() {

    private val waitTime = 2000L
    private var touchTime: Long = 0
    private var badge: Badge? = null

    @Inject
    lateinit var msgService: MsgService

    override fun setLayoutId(): Int = R.layout.fragment_main

    override fun bindVariableId(): Int = BR.viewModel

    override fun initView() {
        val fragments = listOf<Fragment>(
            FragmentSession {
                updateBadgeNumber(this)
            },
            FragmentContact(),
            FragmentMe()
        )

        val titles = listOf(
            R.string.session,
            R.string.contact,
            R.string.me
        )

        content.adapter = DefaultIntFragmentPagerAdapter(childFragmentManager, fragments, titles)
        bottom.setupWithViewPager(content)
        badge = bottom.plusBadge(0)
        badge?.setOnDragStateChangedListener { dragState, _, _ ->
            if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState) {
                msgService.clearAllUnreadCount()
            }
        }
    }

    /**
     * 刷新小红点
     */
    private fun updateBadgeNumber(number: Int) {
        badge?.badgeNumber = number
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NimEventManager.regist()
    }

    override fun onDestroy() {
        super.onDestroy()
        NimEventManager.unRegist()
    }

    /**
     * 双击退出
     */
    private fun initBackListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (System.currentTimeMillis() - touchTime < waitTime) {
                //退出app并清除任务栈
                requireActivity().finish()
            } else {
                touchTime = System.currentTimeMillis()
                R.string.press_again_to_exit.toast()
            }
        }
    }

}