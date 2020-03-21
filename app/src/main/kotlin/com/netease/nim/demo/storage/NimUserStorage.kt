package com.netease.nim.demo.storage

import com.tencent.mmkv.MMKV
import com.zhuzichu.android.shared.storage.BooleanPreference
import com.zhuzichu.android.shared.storage.StringPreference

object NimUserStorage {

    private const val PREFS_NAME = "mmkv_user"

    private val prefs: Lazy<MMKV> = lazy {
        MMKV.mmkvWithID(PREFS_NAME)
    }

    var account by StringPreference(prefs, null)
    var token by StringPreference(prefs, null)
    var notifyToggle by BooleanPreference(prefs, true)

    fun logout() {
        account = null
        token = null
    }

    fun isLogin(): Boolean {
        return !account.isNullOrEmpty() && !token.isNullOrEmpty()
    }
}