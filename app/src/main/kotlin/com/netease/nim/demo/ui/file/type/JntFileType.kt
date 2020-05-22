package com.netease.nim.demo.ui.file.type

import com.netease.nim.demo.R


/**
 * desc
 * author: 朱子楚
 * time: 2020/5/12 2:51 PM
 * since: v 1.0.0
 */
class JntFileType : FileType {

    override val fileType: String
        get() = "Jnt"
    override val fileIconResId: Int
        get() = R.mipmap.file_ic_detail_jnt

    override fun verify(fileName: String): Boolean {
        return when (ToolFileType.getSuffix(fileName)) {
            "jnt" -> {
                true
            }
            else -> {
                false
            }
        }
    }
}