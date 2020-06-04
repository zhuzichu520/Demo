package com.netease.nim.demo.ui.contact.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentContactBinding
import com.netease.nim.demo.nim.event.LoginSyncDataStatusObserver
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.contact.viewmodel.ItemViewModelContactIndex
import com.netease.nim.demo.ui.contact.viewmodel.ViewModelContact
import com.netease.nim.demo.view.QuickIndexBar
import kotlinx.android.synthetic.main.fragment_contact.*

/**
 * desc 通讯录Fragment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentContact : FragmentBase<FragmentContactBinding, ViewModelContact, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_contact

    override fun initOneData() {
        super.initOneData()
        viewModel.updateFriends()
    }

    override fun initListener() {
        super.initListener()
        view_index.setOnTouchingLetterChangedListener(object :
            QuickIndexBar.OnTouchingLetterChangedListener {

            override fun onHit(letter: String) {
                viewModel.letter.value = letter
                viewModel.showLetter.value = true
                scrollToLetter(letter)
            }

            override fun onCancel() {
                viewModel.showLetter.value = false
            }

        })
    }

    private fun scrollToLetter(letter: String) {
        viewModel.items.value?.let {
            it.forEachIndexed { index, any ->
                if (any is ItemViewModelContactIndex) {
                    if (any.letter == letter) {
                        (recycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            index,
                            0
                        )
                        return@let
                    }
                }
            }
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        //登录同步信息完成后更新列表
        LoginSyncDataStatusObserver.getInstance()
            .observeSyncDataCompletedEvent {
                viewModel.updateFriends()
            }

        //好友关系发生变化更新列表
        viewModel.toObservable(NimEvent.OnAddedOrUpdatedFriendsEvent::class.java, Observer {
            viewModel.updateFriends()
        })

        //好友删除更新列表
        viewModel.toObservable(NimEvent.OnDeletedFriendsEvent::class.java, Observer {
            viewModel.updateFriends()
        })

        //用户资料发生变化更新列表
        viewModel.toObservable(NimEvent.OnUserInfoUpdateEvent::class.java, Observer {
            viewModel.updateFriends()
        })

    }

}