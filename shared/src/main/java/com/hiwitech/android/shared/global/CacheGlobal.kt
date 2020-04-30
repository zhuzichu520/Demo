package com.hiwitech.android.shared.global

import com.hiwitech.android.libs.tool.isExternalStorageWriteable
import com.hiwitech.android.shared.global.AppGlobal.context
import java.io.File

object CacheGlobal {

    private const val CACHE_GLIDE_FILE_NAME = "cache_glide"

    private const val CACHE_MMKV_FILE_NAME = "cache_mmkv"

    private const val CACHE_LOG_FILE_NAME = "cache_log"

    private const val CACHE_NIM_FILE_NAME = "cache_nim"

    private const val CACHE_LUBAN_FILE_NAME = "cache_luban"

    fun initDir() {
        getGlideCacheDir()
    }

    fun getLogCacheDir(): String {
        return getDiskCacheDir(CACHE_LOG_FILE_NAME).absolutePath
    }

    fun getMmkvCacheDir(): String {
        return getDiskCacheDir(CACHE_MMKV_FILE_NAME).absolutePath
    }

    fun getGlideCacheDir(): String {
        return getDiskCacheDir(CACHE_GLIDE_FILE_NAME).absolutePath
    }

    fun getNimCacheDir(): String {
        return getDiskCacheDir(CACHE_NIM_FILE_NAME).absolutePath
    }

    fun getLubanCacheDir(): String {
        return getDiskCacheDir(CACHE_LUBAN_FILE_NAME).absolutePath
    }

    private fun getBaseDiskCacheDir(): File {
        return if (isExternalStorageWriteable()) {
            context.externalCacheDir!!
        } else {
            context.cacheDir
        }
    }

    private fun getDiskCacheDir(last: String): File {
        val file = File(getBaseDiskCacheDir().toString(), last)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absoluteFile
    }

}