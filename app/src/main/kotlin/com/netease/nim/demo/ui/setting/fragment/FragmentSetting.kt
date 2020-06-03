package com.netease.nim.demo.ui.setting.fragment

import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentSettingBinding
import com.netease.nim.demo.ui.setting.viewmodel.ViewModelSetting

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/1 8:40 PM
 * since: v 1.0.0
 */
class FragmentSetting : FragmentBase<FragmentSettingBinding, ViewModelSetting, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_setting

}