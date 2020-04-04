package com.hiwitech.android.shared.widget.page

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseItemViewModel
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.mvvm.databinding.BindingCommand

class ItemViewModelNetworkFooter(
    viewModel: BaseViewModel<*>,
    val onClickRetryCommand: BindingCommand<*>?
) : BaseItemViewModel(viewModel) {

    companion object {
        const val STATE_LOADING = 0
        const val STATE_ERROR = 1
        const val STATE_END = 2
        const val STATE_FINISH = 3
        const val STATE_DEFAULT = 4
    }

    val state = MutableLiveData(STATE_DEFAULT)

}