package com.netease.nim.demo.ui.profile.fragment

import android.text.InputType
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.hiwitech.android.libs.tool.isEmail
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.bus.LiveDataBus
import com.hiwitech.android.shared.ext.showSnackbar
import com.hiwitech.android.shared.tools.ToolRegex
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
                    R.string.input_name_info,
                    InputType.TYPE_CLASS_TEXT,
                    8
                )
                UserInfoFieldEnum.MOBILE -> showInputDialog(
                    it.userInfoFieldEnum,
                    it,
                    R.string.input_mobile_info,
                    InputType.TYPE_CLASS_PHONE
                )
                UserInfoFieldEnum.EMAIL -> showInputDialog(
                    it.userInfoFieldEnum,
                    it,
                    R.string.input_emial_info,
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                )
                UserInfoFieldEnum.SIGNATURE -> showInputDialog(
                    it.userInfoFieldEnum,
                    it,
                    R.string.input_signature_info,
                    maxLength = 100
                )
                else -> {

                }
            }
        })
    }

    private fun showInputDialog(
        userInfoFieldEnum: UserInfoFieldEnum,
        itemViewModel: ItemViewModelProfileEdit,
        @StringRes resId: Int,
        inputType: Int = InputType.TYPE_CLASS_TEXT,
        maxLength: Int? = null
    ) {
        var editText = ""
        MaterialDialog(requireContext()).show {
            title(res = resId)
            input(
                maxLength = maxLength,
                prefill = itemViewModel.text ?: "",
                inputType = inputType,
                waitForPositiveButton = false
            ) { dialog, text ->
                editText = text.toString()
                checkInput(editText, userInfoFieldEnum, dialog)
            }.positiveButton(res = R.string.confirm) {
                viewModel.updateUserInfo(userInfoFieldEnum, editText) {
                    itemViewModel.text = it
                    itemViewModel.content.value = itemViewModel.text
                    LiveDataBus.post(EventProfile.OnUpdateUserInfoEvent())
                }
            }.negativeButton(res = R.string.cancel) {

            }
        }
    }

    private fun checkInput(editText: String, fieldEnum: UserInfoFieldEnum, dialog: MaterialDialog) {
        val inputField = dialog.getInputField()
        val isValid: Boolean
        val error: String?
        when (fieldEnum) {
            UserInfoFieldEnum.EMAIL -> {
                isValid = isEmail(editText)
                error = "请输入正确邮箱"
            }
            UserInfoFieldEnum.MOBILE -> {
                isValid = ToolRegex.isMobile(editText)
                error = "请输入正确的手机号"
            }
            else -> {
                isValid = true
                error = null
            }
        }
        inputField.error = if (isValid) null else error
        dialog.setActionButtonEnabled(WhichButton.POSITIVE, isValid)
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