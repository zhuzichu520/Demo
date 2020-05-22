package com.netease.nim.demo.ui.file.type

import com.netease.nim.demo.R


/**
 * desc
 * author: 朱子楚
 * time: 2020/5/12 2:51 PM
 * since: v 1.0.0
 */
class PngFileType : FileType {

    override val fileType: String
        get() = "Png"
    override val fileIconResId: Int
        get() = R.mipmap.file_ic_detail_png

    override fun verify(fileName: String): Boolean {
        return when (ToolFileType.getSuffix(fileName)) {
            "png" -> {
                true
            }
            else -> {
                false
            }
        }
    }
}