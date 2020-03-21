package com.netease.nim.demo.ui.main.fragment

import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.databinding.FragmentMainBinding
import com.netease.nim.demo.ui.main.viewmodel.ViewModelMain
import com.netease.nim.demo.ui.contact.fragment.FragmentContact
import com.netease.nim.demo.ui.session.fragment.FragmentSession
import com.zhuzichu.android.mvvm.base.ArgDefault
import com.zhuzichu.android.mvvm.base.BaseFragment
import com.zhuzichu.android.shared.base.DefaultIntFragmentPagerAdapter
import com.zhuzichu.android.shared.ext.setupWithViewPager
import com.zhuzichu.android.shared.ext.toast
import kotlinx.android.synthetic.main.fragment_main.*

class FragmentMain : BaseFragment<FragmentMainBinding, ViewModelMain, ArgDefault>() {

    private val waitTime = 2000L
    private var touchTime: Long = 0

    override fun setLayoutId(): Int = R.layout.fragment_main

    override fun bindVariableId(): Int = BR.viewModel

    override fun initView() {
        val fragments = listOf<Fragment>(
            FragmentSession(),
            FragmentContact()
        )

        val titles = listOf(
            R.string.session,
            R.string.contact
        )

        content.adapter = DefaultIntFragmentPagerAdapter(childFragmentManager, fragments, titles)
        bottom.setupWithViewPager(content)
        initBackListener()
    }

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