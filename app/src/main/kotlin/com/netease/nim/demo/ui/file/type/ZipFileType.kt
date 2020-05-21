package com.netease.nim.demo.ui.file.type

import com.netease.nim.demo.R


/**
 * desc
 * author: 朱子楚
 * time: 2020/5/12 2:51 PM
 * since: v 1.0.0
 */
class ZipFileType : FileType {

    override val fileType: String
        get() = "Zip"
    override val fileIconResId: Int
        get() = R.mipmap.file_ic_detail_zip

    override fun verify(fileName: String): Boolean {
        /**
         * 使用 endWith 是不可靠的，因为文件名有可能是以格式结尾，但是没有 . 符号
         * 比如 文件名仅为：example_png
         */
        val isHasSuffix = fileName.contains(".")
        if (!isHasSuffix) {
            // 如果没有 . 符号，即是没有文件后缀
            return false
        }
        return when (ToolFileType.getSuffix(fileName)) {
            "zip" -> {
                true
            }
            else -> {
                false
            }
        }
    }
}