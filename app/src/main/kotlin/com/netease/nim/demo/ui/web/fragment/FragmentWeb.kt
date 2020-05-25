package com.netease.nim.demo.ui.web.fragment

import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.activity.addCallback
import com.hiwitech.android.shared.ext.toast
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentWebBinding
import com.netease.nim.demo.ui.web.AgentWebSettings
import com.netease.nim.demo.ui.web.arg.ArgWeb
import com.netease.nim.demo.ui.web.viewmodel.ViewModelWeb
import com.netease.nim.demo.webview.PreloadWebView
import kotlinx.android.synthetic.main.fragment_web.*

/**
 * desc 个人中心fragment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentWeb : FragmentBase<FragmentWebBinding, ViewModelWeb, ArgWeb>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_web

    private lateinit var agentWeb: AgentWeb

    private val webViewClient: WebViewClient = object : WebViewClient() {

    }

    private val webChromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
        }

    }

    override fun initView() {
        super.initView()
        agentWeb = AgentWeb.with(requireActivity())
            .setAgentWebParent(content, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebView(PreloadWebView.getWebView(requireActivity()))
            .setAgentWebWebSettings(AgentWebSettings(requireActivity()))
            .setWebViewClient(webViewClient)
            .setWebChromeClient(webChromeClient)
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl()
            .createAgentWeb()
            .ready()
            .go(arg.url)
        initBackListener()
    }

    /**
     * 双击退出
     */
    private fun initBackListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (!agentWeb.back()) {
                requireActivity().finish()
            }
        }
    }


}