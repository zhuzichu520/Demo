package com.netease.nim.demo.ui.multiport.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.BaseViewModel
import com.hiwitech.android.shared.ext.autoLoading
import com.hiwitech.android.shared.ext.createCommand
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ItemViewModelBase
import com.netease.nim.demo.ui.multiport.domain.UseCaseKickOtherOut
import com.netease.nimlib.sdk.auth.ClientType
import com.netease.nimlib.sdk.auth.OnlineClient
import com.uber.autodispose.autoDispose

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/4 5:41 PM
 * since: v 1.0.0
 */
class ItemViewModelMultiportOption(
    viewModel: BaseViewModel<*>,
    onlineClient: OnlineClient,
    useCaseKickOtherOut: UseCaseKickOtherOut
) : ItemViewModelBase(viewModel) {

    val title = MutableLiveData<Int>().apply {
        value = when (onlineClient.clientType) {
            ClientType.Windows, ClientType.MAC -> {
                R.string.computer_version
            }
            ClientType.Web -> {
                R.string.web_version
            }
            ClientType.iOS, ClientType.Android -> {
                R.string.mobile_version
            }
            else -> {
                null
            }
        }
    }

    val onClickCommand = createCommand {
        useCaseKickOtherOut.execute(onlineClient)
            .autoLoading(viewModel)
            .autoDispose(viewModel).subscribe {

            }
    }

}