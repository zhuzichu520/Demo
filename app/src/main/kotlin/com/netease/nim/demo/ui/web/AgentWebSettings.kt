package com.netease.nim.demo.ui.web

import android.app.Activity
import android.webkit.WebSettings
import android.webkit.WebView
import com.just.agentweb.AbsAgentWebSettings
import com.just.agentweb.AgentWeb
import com.just.agentweb.IAgentWebSettings

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/25 5:08 PM
 * since: v 1.0.0
 */
class AgentWebSettings(
    val activity: Activity
) : AbsAgentWebSettings() {
    private lateinit var agentWeb: AgentWeb

    override fun bindAgentWebSupport(agentWeb: AgentWeb) {
        this.agentWeb = agentWeb
    }

    override fun toSetting(webView: WebView): IAgentWebSettings<*> {
        super.toSetting(webView)
        webSettings.apply {
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            setSupportZoom(true)
            builtInZoomControls = true
            useWideViewPort = true
            setSupportMultipleWindows(false)
            //是否阻塞加载网络图片  协议http or https
            blockNetworkImage = false
            //允许加载本地文件html  file协议, 这可能会造成不安全 , 建议重写关闭
            allowFileAccess = false
            //通过 file mUrl 加载的 Javascript 读取其他的本地文件 .建议关闭
            allowFileAccessFromFileURLs = false
            //允许通过 file mUrl 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
            allowUniversalAccessFromFileURLs = false
            setNeedInitialFocus(true)
            //设置编码格式
            defaultTextEncodingName = "gb2312"
            defaultFontSize = 16
            //设置 WebView 支持的最小字体大小，默认为 8
            minimumFontSize = 12
            setGeolocationEnabled(true)
            userAgentString = userAgentString.plus("agentweb/3.1.0")
        }
        return this
    }


}