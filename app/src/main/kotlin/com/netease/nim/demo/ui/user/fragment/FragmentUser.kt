package com.netease.nim.demo.ui.user.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.hiwitech.android.shared.ext.showSnackbar
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentUserBinding
import com.netease.nim.demo.nim.event.NimEvent
import com.netease.nim.demo.ui.profile.viewmodel.ItemViewModelProfileEdit
import com.netease.nim.demo.ui.user.arg.ArgUser
import com.netease.nim.demo.ui.user.viewmodel.ViewModelUser
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/3 2:29 PM
 * since: v 1.0.0
 */
class FragmentUser : FragmentBase<FragmentUserBinding, ViewModelUser, ArgUser>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_user

    override fun initOneData() {
        super.initOneData()
        viewModel.updateUserInfo()
    }

    override fun initViewObservable() {
        super.initViewObservable()

        viewModel.onClickDeleteEvent.observe(viewLifecycleOwner, Observer {
            showSnackbar()
        })

        //好友关系发生变化更新列表
        viewModel.toObservable(NimEvent.OnAddedOrUpdatedFriendsEvent::class.java, Observer {
            viewModel.updateUserInfo()
        })

        //好友删除更新列表
        viewModel.toObservable(NimEvent.OnDeletedFriendsEvent::class.java, Observer {
            viewModel.updateUserInfo()
        })

        //用户资料发生变化更新列表
        viewModel.toObservable(NimEvent.OnUserInfoUpdateEvent::class.java, Observer {
            viewModel.updateUserInfo()
        })

        viewModel.onClickAliasEvent.observe(viewLifecycleOwner, Observer {
            showAliasDialog(it)
        })

    }

    /**
     * 编辑别名
     */
    private fun showAliasDialog(itemViewModel: ItemViewModelProfileEdit) {
        MaterialDialog(requireContext()).show {
            title(res = R.string.alias_info)
            input(maxLength = 8, prefill = itemViewModel.text ?: "") { _, text ->
                viewModel.updateAlias(text.toString()) {
                    itemViewModel.content.value = it
                }
            }.negativeButton(res = R.string.cancel) {

            }
        }
    }


    private fun showSnackbar() {
        root.showSnackbar(
            resId = R.string.delete_friend_info,
            duration = 3000,
            actionId = R.string.confirm,
            onClickListener = View.OnClickListener {
                viewModel.deleteFriend()
            }
        )
    }

}