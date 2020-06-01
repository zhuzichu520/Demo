package com.netease.nim.demo.ui.session.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.ext.closeDefaultAnimator
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.SharedViewModel
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentSessionBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.popup.PopupMenus
import com.netease.nim.demo.ui.session.viewmodel.ViewModelSession
import kotlinx.android.synthetic.main.fragment_session.*

/**
 * desc 会话列表Fragment
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
class FragmentSession : FragmentBase<FragmentSessionBinding, ViewModelSession, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_session

    private val shareViewModel by activityViewModels<SharedViewModel>()

    companion object {
        //置顶
        const val TYPE_ON_STICKY_TOP = 1
        //取消置顶
        const val TYPE_CLEAR_STICKY_TOP = 2
        //免打扰
        const val TYPE_ON_DISTURB = 3
        //取消免打扰
        const val TYPE_CLEAR_DISTURB = 4
        //移除
        const val TYPE__REMOVE = 5
    }


    override fun initView() {
        super.initView()
        recycler.closeDefaultAnimator()
    }

    override fun initLazyData() {
        super.initLazyData()
        viewModel.loadSessionList()
    }


    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.sessionList.observe(viewLifecycleOwner, Observer {
            var number = 0
            it.forEach { item ->
                number += item.contact.unreadCount
            }
            shareViewModel.onSessionNumberChangeEvent.value = number
        })

        viewModel.toObservable(NimEvent.OnRecentContactEvent::class.java, Observer {
            viewModel.parseSessionList(it.list)
        })

        viewModel.onLongClickItemEvent.observe(viewLifecycleOwner, Observer {
            PopupMenus(
                requireContext(), listOf(
                    if (it.isTop.value == true) {
                        PopupMenus.ItemMenu(TYPE_CLEAR_STICKY_TOP, R.string.clear_sticky_top)
                    } else {
                        PopupMenus.ItemMenu(TYPE_ON_STICKY_TOP, R.string.on_sticky_top)
                    },
                    PopupMenus.ItemMenu(TYPE_ON_DISTURB, R.string.on_disturb),
                    PopupMenus.ItemMenu(TYPE__REMOVE, R.string.remove)
                )
            ) {
                when (this.type) {
                    TYPE_ON_STICKY_TOP -> {
                        it.top()
                    }
                    TYPE_CLEAR_STICKY_TOP -> {
                        it.unTop()
                    }
                    TYPE_ON_DISTURB -> {
                    }
                    TYPE_CLEAR_DISTURB -> {
                    }
                    TYPE__REMOVE -> {
                        viewModel.deleteSession(it)
                    }
                }
            }.show(it.touchPostionArr[0].toInt(), it.touchPostionArr[1].toInt())
        })
    }

}