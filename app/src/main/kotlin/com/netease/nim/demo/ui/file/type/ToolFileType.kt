package com.netease.nim.demo.ui.file.type

/**
 * desc  文件类型工具类
 * author: 朱子楚
 * time: 2020/5/12 2:51 PM
 * since: v 1.0.0
 */
object ToolFileType {

    val allFileType: ArrayList<FileType> by lazy {
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
     * 获取文件后缀名
     * @param fileName 文件名称
     */
    fun getSuffix(fileName: String): String {
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