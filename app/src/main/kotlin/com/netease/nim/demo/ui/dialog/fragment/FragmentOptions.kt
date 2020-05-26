package com.netease.nim.demo.ui.dialog.fragment

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.BottomDialogFragmentBase
import com.netease.nim.demo.databinding.DialogFragmentOptionsBinding
import com.netease.nim.demo.ui.dialog.entity.EntityOptions
import com.netease.nim.demo.ui.dialog.viewmodel.ItemViewModelOptions
import com.netease.nim.demo.ui.dialog.viewmodel.ViewModelOptions

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/26 9:51 AM
 * since: v 1.0.0
 */
class FragmentOptions :
    BottomDialogFragmentBase<DialogFragmentOptionsBinding, ViewModelOptions, ArgDefault>() {

    private lateinit var options: List<EntityOptions>

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.dialog_fragment_options

    fun show(options: List<EntityOptions>, fragmentManager: FragmentManager) {
        this.options = options
        showNow(fragmentManager, "FragmentOptions")
    }

    override fun initView() {
        super.initView()
        viewModel.items.value = options.map {
            ItemViewModelOptions(viewModel, it)
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.onDialogDismissEvent.observe(viewLifecycleOwner, Observer {
            dismiss()
        })
    }


}