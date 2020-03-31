package com.netease.nim.demo.ui.message.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.nim.repository.NimRepository
import com.netease.nim.demo.ui.message.main.arg.ArgMessage
import com.uber.autodispose.autoDispose
import com.zhuzichu.android.shared.ext.map
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

class ViewModelMessage @Inject constructor(
    private val nimRepository: NimRepository
) : ViewModelBase<ArgMessage>() {

    val title = MutableLiveData<String>()

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelTextMessage>(BR.item, R.layout.item_message_text)
        map<ItemViewModelPictureMessage>(BR.item, R.layout.item_message_picture)
    }

    val items = MutableLiveData<List<Any>>()


    fun loadUserInfo() {
        nimRepository.getUserInfoById(arg.contactId)
            .autoDispose(this)
            .subscribe(
                {
                    title.value = it.name
                },
                {
                    handleThrowable(it)
                }
            )

    }

    fun loadTeamInfo() {
        nimRepository.getTeamById(arg.contactId)
            .autoDispose(this)
            .subscribe(
                {
                    title.value = it.name
                },
                {
                    handleThrowable(it)
                }
            )
    }


}