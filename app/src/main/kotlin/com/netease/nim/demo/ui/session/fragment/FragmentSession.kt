package com.netease.nim.demo.ui.session.fragment

import androidx.lifecycle.Observer
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentSessionBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.session.viewmodel.ViewModelSession
import com.uber.autodispose.autoDispose
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.bus.RxBus
import com.hiwitech.android.shared.ext.bindToSchedulers

class FragmentSession(
    private val closure: Int.() -> Unit
) : FragmentBase<FragmentSessionBinding, ViewModelSession, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_session

    override fun initLazyData() {
        super.initLazyData()
        viewModel.loadSessionList()
    }

    override fun initViewObservable() {
        super.initViewObservable()
        RxBus.toObservable(NimEvent.OnRecentContactEvent::class.java)
            .bindToSchedulers()
            .autoDispose(viewModel)
            .subscribe {
                viewModel.parseSessionList(it.list)
            }

        viewModel.sessionList.observe(viewLifecycleOwner, Observer {
            var number = 0
            it.forEach { item ->
                number += item.contact.unreadCount
            }
            closure.invoke(number)
        })

    }

}