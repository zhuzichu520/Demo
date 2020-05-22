package com.netease.nim.demo.ui.file.type

/**
 * desc  文件类型工具类
 * author: 朱子楚
 * time: 2020/5/12 2:51 PM
 * since: v 1.0.0
 */
object ToolFileType {

    private val allFileType: ArrayList<FileType> by lazy {
        val fileTypes = ArrayList<FileType>()
        fileTypes.add(ApkFileType())
        fileTypes.add(AppFileType())
        fileTypes.add(BmpFileType())
        fileTypes.add(CsvFileType())
        fileTypes.add(ExcelFileType())
        fileTypes.add(ExeFileType())
        fileTypes.add(GifFileType())
        fileTypes.add(HtmlFileType())
        fileTypes.add(JntFileType())
        fileTypes.add(JpgFileType())
        fileTypes.add(Mp3FileType())
        fileTypes.add(Mp4FileType())
        fileTypes.add(PdfFileType())
        fileTypes.add(PhpFileType())
        fileTypes.add(PngFileType())
        fileTypes.add(PptFileType())
        fileTypes.add(RarFileType())
        fileTypes.add(RmvbFileType())
        fileTypes.add(TxtFileType())
        fileTypes.add(WordFileType())
        fileTypes.add(ZipFileType())
        fileTypes
    }

    /**
     * 通过文件名获取文件类型
     * @param fileName 文件名
     */
    fun getFileTypeByFileName(fileName: String): FileType? {
        var fileType: FileType? = null
        allFileType.forEach {
            if (it.verify(fileName)) {
                fileType = it
                return@forEach
            }
        }
        return fileType
    }

    /**
     * 获取文件后缀名
     * @param fileName 文件名称
     */
    fun getSuffix(fileName: String): String? {
        val isHasSuffix = fileName.contains(".")
        if (!isHasSuffix) {
            return null
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1)
    }

    /**
     * 支持预览
     * @param fileType 文件类型
     */
    fun isPreViewFile(fileType: FileType?): Boolean {
        if (fileType == null) {
            return false
        }
        return fileType is WordFileType || fileType is ExcelFileType || fileType is PptFileType
    }

}