package com.netease.nim.demo.ui.contact.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.shared.ext.map
import com.hiwitech.android.shared.global.AppGlobal.context
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.ui.contact.domain.UseCaseGetFriendInfoList
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import com.uber.autodispose.autoDispose
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

/**
 * desc 通讯录ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelContact @Inject constructor(
    private val useCaseGetFriendInfoList: UseCaseGetFriendInfoList
) : ViewModelBase<ArgDefault>() {


    val items = MutableLiveData<List<Any>>()

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelContact>(BR.item, R.layout.item_contact)
        map<ItemViewModelContactIndex>(BR.item, R.layout.item_contact_index)
    }

    val letter = MutableLiveData<String>()

    val showLetter = MutableLiveData<Boolean>(false)

    fun loadFriends() {
        useCaseGetFriendInfoList.execute(Unit)
            .map {
                convertItemViewModel(it)
            }
            .autoDispose(this)
            .subscribe {
                items.value = it
            }
    }

    private fun convertItemViewModel(it: List<NimUserInfo>): List<Any> {
        val data = it.map { item ->
            ItemViewModelContact(this, item)
        }
        val map = hashMapOf<String, ArrayList<ItemViewModelContact>?>()
        val letters = context.resources.getStringArray(R.array.letter_list)

        letters.forEach { letter ->
            map[letter] = null
        }

        data.forEach { item ->
            var letter = "#"
            val list = if (Character.isUpperCase(item.pinYin)) {
                letter = item.pinYin.toString()
                map[letter] ?: arrayListOf()
            } else {
                map[letter] ?: arrayListOf()
            }
            list.add(item)
            map[letter] = list
        }
        val list = arrayListOf<Any>()
        map.forEach { entry ->
            entry.value?.let { value ->
                list.add(ItemViewModelContactIndex(this, entry.key))
                value.forEach { item ->
                    list.add(item)
                }
            }
        }
        return list
    }

}