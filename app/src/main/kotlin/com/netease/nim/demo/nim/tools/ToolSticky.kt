package com.netease.nim.demo.nim.tools

import com.hiwitech.android.libs.tool.toCast
import com.netease.nimlib.sdk.msg.model.RecentContact

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/10 3:18 PM
 * since: v 1.0.0
 */
class ToolSticky {
    companion object {

        const val SESSION_ON_TOP = "SESSION_ON_TOP"

        fun addStickyTag(contact: RecentContact) {
            val extension = contact.extension ?: mutableMapOf()
            extension[SESSION_ON_TOP] = System.currentTimeMillis()
            contact.extension = extension
        }

        fun removeStickTag(contact: RecentContact) {
            val extension = contact.extension ?: mutableMapOf()
            extension[SESSION_ON_TOP] = null
            contact.extension = extension
        }

        fun isStickyTagSet(contact: RecentContact): Boolean {
            val extension = contact.extension ?: return false
            extension[SESSION_ON_TOP] ?: return false
            return true
        }

        fun getStickyLong(contact: RecentContact): Long {
            val map = contact.extension ?: mapOf()
            return (map[SESSION_ON_TOP] ?: Long.MIN_VALUE).toCast()
        }

    }
}