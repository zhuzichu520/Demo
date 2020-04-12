package com.netease.nim.demo.storage

import com.hiwitech.android.shared.storage.BooleanPreference
import com.hiwitech.android.shared.storage.IntPreference
import com.hiwitech.android.shared.storage.StringPreference
import com.netease.nimlib.sdk.auth.LoginInfo
import com.tencent.mmkv.MMKV

/**
 * desc IM User 存储
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
object NimUserStorage {

    private const val PREFS_NAME = "mmkv_user"

    private val prefs: Lazy<MMKV> = lazy {
        MMKV.mmkvWithID(PREFS_NAME)
    }

    var account by StringPreference(prefs, null)
    var token by StringPreference(prefs, null)
    var notifyToggle by BooleanPreference(prefs, true)
    var softKeyboardHeight by IntPreference(prefs, 0)

    fun logout() {
        account = null
        token = null
    }

    fun isLogin(): Boolean {
        return !account.isNullOrEmpty() && !token.isNullOrEmpty()
    }

    fun login(loginInfo: LoginInfo) {
        account = loginInfo.account
        token = loginInfo.token
    }


}