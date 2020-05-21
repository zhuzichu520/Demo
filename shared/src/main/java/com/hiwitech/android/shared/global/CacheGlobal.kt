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

    private const val CACHE_CAMERA_FILE_NAME = "cache_camera"

    private const val CACHE_TBSREADERTEMP_FILE_NAME="TbsReaderTemp"

    fun initDir() {
        getGlideCacheDir()
    }

    fun getLogCacheDir(): String {
        return getDiskCacheDir(child = CACHE_LOG_FILE_NAME).absolutePath
    }

    fun getMmkvCacheDir(): String {
        return getDiskCacheDir(child = CACHE_MMKV_FILE_NAME).absolutePath
    }

    fun getGlideCacheDir(): String {
        return getDiskCacheDir(child = CACHE_GLIDE_FILE_NAME).absolutePath
    }

    fun getNimCacheDir(): String {
        return getDiskCacheDir(child = CACHE_NIM_FILE_NAME).absolutePath
    }

    fun getNimCacheVideoDir(): String {
        return getDiskCacheDir(getNimCacheDir(), "video").absolutePath
    }

    fun getLubanCacheDir(): String {
        return getDiskCacheDir(child = CACHE_LUBAN_FILE_NAME).absolutePath
    }

    fun getTbsReaderTempCacheDir(): String {
        return getDiskCacheDir(child = CACHE_TBSREADERTEMP_FILE_NAME).absolutePath
    }

    fun getCameraDir(): String {
        return getDiskCacheDir(child = CACHE_CAMERA_FILE_NAME).absolutePath
    }

    private fun getBaseDiskCacheDir(): File? {
        return if (isExternalStorageWriteable()) {
            context.externalCacheDir
        } else {
            context.cacheDir
        }
    }

    private fun getDiskCacheDir(
        parent: String? = getBaseDiskCacheDir().toString(),
        child: String
    ): File {
        val file = File(parent, child)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absoluteFile
    }

}