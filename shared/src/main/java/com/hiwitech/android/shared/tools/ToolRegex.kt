package com.hiwitech.android.shared.tools

import java.util.regex.Pattern

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/12 5:15 PM
 * since: v 1.0.0
 */
object ToolRegex {

    private const val REGEX_MOBILE =
        "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}\$"

    fun isMobile(mobile: String?): Boolean =
        !mobile.isNullOrEmpty() && Pattern.matches(REGEX_MOBILE, mobile)
}