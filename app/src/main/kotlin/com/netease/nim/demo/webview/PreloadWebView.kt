package com.netease.nim.demo.webview

import android.content.Context
import android.content.MutableContextWrapper
import android.os.Looper
import android.webkit.WebView
import com.hiwitech.android.shared.global.AppGlobal.context
import java.util.*


/**
 * desc
 * author: 朱子楚
 * time: 2020/5/25 10:19 AM
 * since: v 1.0.0
 */
object PreloadWebView {

    private const val CACHED_WEBVIEW_MAX_NUM = 2
    private val mCachedWebViewStack: Stack<WebView> = Stack()


    /**
     * 创建WebView实例
     * 用了applicationContext
     */
    fun preload() {
        Looper.myQueue().addIdleHandler {
            if (mCachedWebViewStack.size < CACHED_WEBVIEW_MAX_NUM) {
                mCachedWebViewStack.push(createWebView())
            }
            false
        }
    }

    private fun createWebView(): WebView {
        return WebView(MutableContextWrapper(context))
    }


    fun getWebView(context: Context): WebView { // 为空，直接返回新实例
        if (mCachedWebViewStack.isEmpty()) {
            val web = createWebView()
            val contextWrapper = web.context as MutableContextWrapper
            contextWrapper.baseContext = context
            return web
        }
        val webView = mCachedWebViewStack.pop()
        // webView不为空，则开始使用预创建的WebView,并且替换Context
        val contextWrapper = webView.context as MutableContextWrapper
        contextWrapper.baseContext = context
        return webView
    }


}