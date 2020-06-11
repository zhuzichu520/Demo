package com.netease.nim.demo.ui.session.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.ext.closeDefaultAnimator
import com.hiwitech.android.shared.ext.createCommand
import com.hiwitech.android.shared.ext.toStringByResId
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.SharedViewModel
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentSessionBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.main.ActivityMain
import com.netease.nim.demo.ui.multiport.ActivityMultiport
import com.netease.nim.demo.ui.multiport.arg.ArgMultiport
import com.netease.nim.demo.ui.popup.PopupMenus
import com.netease.nim.demo.ui.session.viewmodel.ViewModelSession
import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.ClientType
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

        //用户资料发生变化更新列表
        viewModel.toObservable(NimEvent.OnAddedOrUpdatedFriendsEvent::class.java, Observer {
            viewModel.refresh(it.list.map { friend ->
                friend.account
            })
        })

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

        viewModel.toObservable(NimEvent.OnLineStatusEvent::class.java, Observer {
            if (it.statusCode.wontAutoLogin()) {
                ActivityMain.logout(requireContext(), true)
            } else {
                when (it.statusCode) {
                    StatusCode.NET_BROKEN -> {
                        viewModel.showNetWorkBar(true)
                        viewModel.setNetWorkText(R.string.net_broken)
                    }
                    StatusCode.UNLOGIN -> {
                        viewModel.showNetWorkBar(false)
                        viewModel.setNetWorkText(R.string.nim_status_unlogin)
                    }
                    StatusCode.CONNECTING -> {
                        viewModel.showNetWorkBar(true)
                        viewModel.setNetWorkText(R.string.nim_status_connecting)
                    }
                    StatusCode.LOGINING -> {
                        viewModel.showNetWorkBar(false)
                        viewModel.setNetWorkText(R.string.nim_status_logining)
                    }
                    else -> {
                        viewModel.showNetWorkBar(false)
                    }
                }
            }
        })

        viewModel.toObservable(NimEvent.OnLienClientEvent::class.java, Observer {
            val list = it.list
            if (list.isNullOrEmpty()) {
                viewModel.showMultiportBar(false)
            } else {
                val onlineClient = list[0]
                viewModel.updateMultiportCommand(createCommand {
                    startActivity(ActivityMultiport::class.java, arg = ArgMultiport(list))
                })
                val logging = R.string.multiport_logging.toStringByResId(requireContext())
                when (onlineClient.clientType) {
                    ClientType.Windows, ClientType.MAC -> {
                        viewModel.setMultiportText(
                            logging + R.string.computer_version.toStringByResId(requireContext())
                        )
                        viewModel.showMultiportBar(true)
                    }
                    ClientType.Web -> {
                        viewModel.setMultiportText(
                            logging + R.string.web_version.toStringByResId(requireContext())
                        )
                        viewModel.showMultiportBar(true)
                    }
                    ClientType.iOS, ClientType.Android -> {
                        viewModel.setMultiportText(
                            logging + R.string.mobile_version.toStringByResId(requireContext())
                        )
                        viewModel.showMultiportBar(true)
                    }
                    else -> {
                        viewModel.showMultiportBar(false)
                    }
                }
            }
        })

    }

}