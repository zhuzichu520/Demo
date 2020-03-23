package com.netease.nim.demo.ui.session.fragment

import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentSessionBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.session.viewmodel.ItemViewModelSession
import com.netease.nim.demo.ui.session.viewmodel.ViewModelSession
import com.uber.autodispose.autoDispose
import com.zhuzichu.android.libs.tool.replaceAt
import com.zhuzichu.android.libs.tool.toCast
import com.zhuzichu.android.mvvm.base.ArgDefault
import com.zhuzichu.android.shared.bus.RxBus
import com.zhuzichu.android.shared.ext.bindToSchedulers

class FragmentSession : FragmentBase<FragmentSessionBinding, ViewModelSession, ArgDefault>() {

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
                viewModel.items.value?.apply {
                    it.list.forEach { item ->
                        val session = ItemViewModelSession(viewModel, item)
                        val position = this.indexOf(session)
                        if (position == -1) {
                            this.plus(session)
                        } else {
                            this.replaceAt(position, session.toCast())
                        }
                    }
                    viewModel.items.value = this
                } ?: apply {
                    viewModel.items.value = it.list.map { item ->
                        ItemViewModelSession(viewModel, item)
                    }
                }

            }
    }

}