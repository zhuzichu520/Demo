package com.netease.nim.demo.ui.profile.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.map
import com.hiwitech.android.shared.ext.toStringByResId
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetUserInfo
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum
import com.uber.autodispose.autoDispose
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import javax.inject.Inject

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 11:40 AM
 * since: v 1.0.0
 */
class ViewModelProfile @Inject constructor(
    private val useCaseGetUserInfo: UseCaseGetUserInfo
) : ViewModelBase<ArgDefault>() {

    val items = MutableLiveData<List<Any>>()

    val onLogOutEvent = SingleLiveEvent<Unit>()

    /**
     * 多布局注册
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelProfileEdit>(BR.item, R.layout.item_profile_edit)
        map<ItemViewModelProfileHeader>(BR.item, R.layout.item_profile_header)
        map<ItemViewModelProfileLogout>(BR.item, R.layout.item_profile_logout)
    }


    fun loadUserInfo() {
        useCaseGetUserInfo.execute(NimUserStorage.account.toString())
            .autoDispose(this)
            .subscribe {
                items.value = listOf(
                    ItemViewModelProfileHeader(this, it.avatar),
                    ItemViewModelProfileEdit(this, R.string.nickname, it.name),
                    ItemViewModelProfileEdit(
                        this, R.string.gender, when (it.genderEnum) {
                            GenderEnum.MALE -> {
                                R.string.male.toStringByResId()
                            }
                            GenderEnum.FEMALE -> {
                                R.string.female.toStringByResId()
                            }
                            else -> R.string.other.toStringByResId()
                        }
                    ),
                    ItemViewModelProfileEdit(this, R.string.birthday, it.birthday),
                    ItemViewModelProfileEdit(this, R.string.phone, it.mobile),
                    ItemViewModelProfileEdit(this, R.string.email, it.email),
                    ItemViewModelProfileEdit(this, R.string.signature, it.signature),
                    ItemViewModelProfileLogout(this)
                )
            }
    }

}