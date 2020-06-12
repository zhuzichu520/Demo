package com.netease.nim.demo.ui.profile.viewmodel

import androidx.lifecycle.MutableLiveData
import com.hiwitech.android.mvvm.base.ArgDefault
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.autoLoading
import com.hiwitech.android.shared.ext.map
import com.hiwitech.android.shared.ext.toStringByResId
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.ViewModelBase
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.message.main.domain.UseCaseGetUserInfo
import com.netease.nim.demo.ui.profile.domain.UseCaseUpdateUserInfo
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
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
    private val useCaseGetUserInfo: UseCaseGetUserInfo,
    private val useCaseUpdateUserInfo: UseCaseUpdateUserInfo
) : ViewModelBase<ArgDefault>() {

    val items = MutableLiveData<List<Any>>()

    val onLogOutEvent = SingleLiveEvent<Unit>()

    val onClickEditEvent = SingleLiveEvent<ItemViewModelProfileEdit>()

    val userInfo = MutableLiveData<NimUserInfo>()

    /**
     * 多布局注册
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelProfileEdit>(BR.item, R.layout.item_profile_edit)
        map<ItemViewModelProfileHeader>(BR.item, R.layout.item_profile_header)
        map<ItemViewModelProfileLogout>(BR.item, R.layout.item_profile_logout)
    }

    fun updateUserInfo(key: UserInfoFieldEnum, value: Any, success: (String) -> Unit) {
        useCaseUpdateUserInfo.execute(mapOf(key to value))
            .autoLoading(this)
            .autoDispose(this)
            .subscribe(
                {
                    success.invoke(value.toString())
                },
                {
                    handleThrowable(it)
                }
            )
    }

    fun getIndexByGender(): Int {
        val userInfo = userInfo.value ?: return 2
        return when (userInfo.genderEnum) {
            GenderEnum.MALE -> 0
            GenderEnum.FEMALE -> 1
            else -> 2
        }
    }

    fun loadUserInfo() {
        useCaseGetUserInfo.execute(NimUserStorage.account.toString())
            .autoDispose(this)
            .subscribe {
                userInfo.value = it.get()
                val userInfo = userInfo.value ?: return@subscribe
                items.value = listOf(
                    ItemViewModelProfileHeader(this, userInfo.avatar),
                    ItemViewModelProfileEdit(
                        this,
                        R.string.nickname,
                        userInfo.name,
                        onClickEditEvent,
                        UserInfoFieldEnum.Name
                    ),
                    ItemViewModelProfileEdit(
                        this,
                        R.string.gender,
                        when (userInfo.genderEnum) {
                            GenderEnum.MALE -> {
                                R.string.male.toStringByResId()
                            }
                            GenderEnum.FEMALE -> {
                                R.string.female.toStringByResId()
                            }
                            else -> R.string.other.toStringByResId()
                        }
                        , onClickEditEvent,
                        UserInfoFieldEnum.GENDER
                    ),
                    ItemViewModelProfileEdit(
                        this,
                        R.string.birthday,
                        userInfo.birthday,
                        onClickEditEvent,
                        UserInfoFieldEnum.BIRTHDAY
                    ),
                    ItemViewModelProfileEdit(
                        this,
                        R.string.phone,
                        userInfo.mobile,
                        onClickEditEvent,
                        UserInfoFieldEnum.MOBILE
                    ),
                    ItemViewModelProfileEdit(
                        this,
                        R.string.email,
                        userInfo.email,
                        onClickEditEvent,
                        UserInfoFieldEnum.EMAIL
                    ),
                    ItemViewModelProfileEdit(
                        this,
                        R.string.signature,
                        userInfo.signature,
                        onClickEditEvent,
                        UserInfoFieldEnum.SIGNATURE
                    ),
                    ItemViewModelProfileLogout(this)
                )
            }
    }

}