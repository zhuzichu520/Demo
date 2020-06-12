package com.netease.nim.demo.ui.profile.fragment

import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.bus.LiveDataBus
import com.hiwitech.android.shared.ext.showSnackbar
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentProfileBinding
import com.netease.nim.demo.ui.main.ActivityMain
import com.netease.nim.demo.ui.profile.event.EventProfile
import com.netease.nim.demo.ui.profile.viewmodel.ItemViewModelProfileEdit
import com.netease.nim.demo.ui.profile.viewmodel.ViewModelProfile
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 11:39 AM
 * since: v 1.0.0
 */
class FragmentProfile : FragmentBase<FragmentProfileBinding, ViewModelProfile, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_profile

    override fun initOneData() {
        super.initOneData()
        viewModel.loadUserInfo()
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.onLogOutEvent.observe(viewLifecycleOwner, Observer {
            showSnackbar()
        })

        viewModel.onClickEditEvent.observe(viewLifecycleOwner, Observer {
            when (it.userInfoFieldEnum) {
                UserInfoFieldEnum.Name -> showInputDialog(
                    it.userInfoFieldEnum,
                    it,
                    R.string.input_name_info
                )
                UserInfoFieldEnum.MOBILE -> showInputDialog(
                    it.userInfoFieldEnum,
                    it,
                    R.string.input_mobile_info
                )
                UserInfoFieldEnum.EMAIL -> showInputDialog(
                    it.userInfoFieldEnum,
                    it,
                    R.string.input_emial_info
                )
                UserInfoFieldEnum.SIGNATURE -> showInputDialog(
                    it.userInfoFieldEnum,
                    it,
                    R.string.input_signature_info
                )
                else -> {

                }
            }
        })
    }

    private fun showInputDialog(
        userInfoFieldEnum: UserInfoFieldEnum,
        itemViewModel: ItemViewModelProfileEdit,
        @StringRes resId: Int
    ) {
        MaterialDialog(requireContext()).show {
            title(res = resId)
            input(maxLength = 8, prefill = itemViewModel.text ?: "") { _, text ->
                viewModel.updateUserInfo(userInfoFieldEnum, text.toString()) {
                    itemViewModel.content.value = it
                    LiveDataBus.post(EventProfile.OnUpdateUserInfoEvent())
                }
            }.negativeButton(res = R.string.cancel) {

            }
        }
    }

    private fun showSnackbar() {
        root.showSnackbar(
            resId = R.string.logout_info,
            duration = 3000,
            actionId = R.string.confirm,
            onClickListener = View.OnClickListener {
                ActivityMain.logout(requireActivity())
            }
        )
    }

}