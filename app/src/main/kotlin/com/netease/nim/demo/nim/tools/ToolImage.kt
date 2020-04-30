package com.netease.nim.demo.nim.tools

import com.hiwitech.android.libs.qmui.QMUIDisplayHelper.getScreenWidth
import com.hiwitech.android.libs.tool.toCast
import com.hiwitech.android.shared.global.AppGlobal.context

class ToolImage {

    class ImageSize(width: Int, height: Int) {
        var width = 0
        var height = 0

        init {
            this.width = width
            this.height = height
        }
    }

    companion object {
        fun getThumbnailDisplaySize(
            srcWidth: Float,
            srcHeight: Float,
            dstMaxWH: Float,
            dstMinWH: Float
        ): ImageSize {
            var width = srcWidth
            var height = srcHeight
            if (width <= 0 || srcHeight <= 0) { // bounds check
                return ImageSize(dstMinWH.toInt(), dstMinWH.toInt())
            }
            var shorter: Float
            var longer: Float
            val widthIsShorter: Boolean
            //store
            if (height < width) {
                shorter = height
                longer = width
                widthIsShorter = false
            } else {
                shorter = width
                longer = height
                widthIsShorter = true
            }
            if (shorter < dstMinWH) {
                val scale = dstMinWH / shorter
                shorter = dstMinWH
                if (longer * scale > dstMaxWH) {
                    longer = dstMaxWH
                } else {
                    longer *= scale
                }
            } else if (longer > dstMaxWH) {
                val scale = dstMaxWH / longer
                longer = dstMaxWH
                if (shorter * scale < dstMinWH) {
                    shorter = dstMinWH
                } else {
                    shorter *= scale
                }
            }
            //restore
            if (widthIsShorter) {
                width = shorter
                height = longer
            } else {
                width = longer
                height = shorter
            }
            return ImageSize(width.toInt(), height.toInt())
        }

        fun getImageMaxEdge(): Int {
            return (165.0 / 320.0 * getScreenWidth(context)).toCast()
        }

        fun getImageMinEdge(): Int {
            return (76.0 / 320.0 * getScreenWidth(context)).toCast()
        }
    }
}