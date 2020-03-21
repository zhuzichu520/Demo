package com.zhuzichu.android.shared.widget.page

import androidx.lifecycle.MutableLiveData
import com.zhuzichu.android.mvvm.base.BaseItemViewModel
import com.zhuzichu.android.mvvm.base.BaseViewModel
import com.zhuzichu.android.mvvm.databinding.BindingCommand

class ItemViewModelNetwork(
    viewModel: BaseViewModel<*>,
    val onClickRetry: BindingCommand<Any>
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