package com.netease.nim.demo.ui.file.type

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/12 2:51 PM
 * since: v 1.0.0
 */
interface FileType {
    /**
     * 文件类型
     */
    val fileType: String

    val fileIconResId: Int
    /**
     * 传入文件路径，判断是否为该类型
     * @param fileName String
     * @return Boolean
     */
    fun verify(fileName: String): Boolean

}