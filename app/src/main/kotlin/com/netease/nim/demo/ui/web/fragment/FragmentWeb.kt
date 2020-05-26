package com.netease.nim.demo.ui.web.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.webkit.ValueCallback
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.hiwitech.android.libs.tool.toCast
import com.hiwitech.android.mvvm.event.SingleLiveEvent
import com.hiwitech.android.shared.ext.autoLoading
import com.hiwitech.android.shared.ext.bindToSchedulers
import com.hiwitech.android.shared.ext.createFlowable
import com.hiwitech.android.shared.ext.toast
import com.hiwitech.android.shared.global.CacheGlobal
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentWebBinding
import com.netease.nim.demo.ui.camera.arg.ArgCamera
import com.netease.nim.demo.ui.camera.event.EventCamera
import com.netease.nim.demo.ui.dialog.entity.EntityOptions
import com.netease.nim.demo.ui.dialog.fragment.FragmentOptions
import com.netease.nim.demo.ui.permissions.fragment.FragmentPermissions
import com.netease.nim.demo.ui.web.AgentWebSettings
import com.netease.nim.demo.ui.web.arg.ArgWeb
import com.netease.nim.demo.ui.web.viewmodel.ViewModelWeb
import com.netease.nim.demo.webview.PreloadWebView
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uber.autodispose.autoDispose
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import kotlinx.android.synthetic.main.fragment_web.*
import top.zibin.luban.Luban
import java.util.*

/**
 * desc 个人中心fragment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentWeb : FragmentBase<FragmentWebBinding, ViewModelWeb, ArgWeb>() {

    companion object {
        private const val REQUEST_CODE_CHOOSE = 0x11
        private const val OPTIONS_ALBUM = 1
        private const val OPTIONS_VIDEO = 2
    }

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_web

    private lateinit var agentWeb: AgentWeb

    private val onClickItemOptionsEvent = SingleLiveEvent<EntityOptions>()

    private var uploadFile: ValueCallback<*>? = null

    private var uploadMultipleFile: ValueCallback<*>? = null

    private val options = listOf(
        EntityOptions(
            OPTIONS_ALBUM,
            R.string.input_panel_album,
            onClickItemOptionsEvent
        ),
        EntityOptions(
            OPTIONS_VIDEO,
            R.string.input_panel_video,
            onClickItemOptionsEvent
        )
    )

    private val webViewClient: WebViewClient = object : WebViewClient() {

    }

    private val webChromeClient: WebChromeClient = object : WebChromeClient() {
        //  Android 2.2 (API level 8)到Android 2.3 (API level 10)版本选择文件时会触发该隐藏方法
        override fun openFileChooser(uploadFile: ValueCallback<Uri>) {
            openFileChooser(uploadFile, null)
        }

        // Android 3.0 (API level 11)到 Android 4.0 (API level 15))版本选择文件时会触发，该方法为隐藏方法
        override fun openFileChooser(uploadFile: ValueCallback<*>, acceptType: String?) {
            openFileChooser(uploadFile.toCast(), acceptType, null);
        }

        // Android 4.1 (API level 16) -- Android 4.3 (API level 18)版本选择文件时会触发，该方法为隐藏方法
        override fun openFileChooser(
            uploadFile: ValueCallback<Uri>,
            acceptType: String?,
            capture: String?
        ) {
            openFileInput(uploadFile, null)
        }

        // Android 5.0 (API level 21)以上版本会触发该方法，该方法为公开方法
        @SuppressLint("ObsoleteSdkInt")
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            openFileInput(null, filePathCallback)
            return true
        }

    }

    private fun openFileInput(
        uploadFile: ValueCallback<Uri>?,
        uploadMultipleFile: ValueCallback<Array<Uri>>?
    ) {
        this.uploadFile = uploadFile
        this.uploadMultipleFile = uploadMultipleFile
        FragmentOptions().let {
            it.show(options, childFragmentManager)
            it.dialog?.setOnCancelListener {
                cancleChooseFile()
            }
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

    override fun initViewObservable() {
        super.initViewObservable()

        onClickItemOptionsEvent.observe(viewLifecycleOwner, Observer {
            when (it.option) {
                OPTIONS_ALBUM -> {
                    startAlbum()
                }
                OPTIONS_VIDEO -> {
                    startCamera()
                }
            }
        })

        /**
         * 拍摄中选择
         */
        viewModel.toObservable(EventCamera.OnCameraEvent::class.java, Observer {
            if (it.type != ArgCamera.TYPE_WEB) {
                return@Observer
            }
            when (it.cameraType) {
                EventCamera.TYPE_NULL -> {
                    cancleChooseFile()
                }
                else -> {
                    handleResult(listOf(it.path))
                }
            }
        })

    }

    /**
     * 跳转到相册
     */
    @SuppressLint("ObsoleteSdkInt")
    private fun startAlbum() {
        RxPermissions(requireActivity()).request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).autoDispose(viewModel).subscribe {
            if (it) {
                Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .countable(true)
                    .maxSelectable(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 9 else 1)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(GlideEngine())
                    .theme(R.style.Widget_MyTheme_Zhihu)
                    .showPreview(false) // Default is `true`
                    .forResult(REQUEST_CODE_CHOOSE)
            } else {
                FragmentPermissions().show("文件读写", childFragmentManager)
            }
        }
    }

    /**
     * 跳转到相机录屏界面
     */
    private fun startCamera() {
        RxPermissions(this).request(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).autoDispose(viewModel).subscribe {
            if (it) {
                start(
                    R.id.action_fragmentWeb_to_activityCamera,
                    arg = ArgCamera(ArgCamera.TYPE_WEB)
                )
            } else {
                FragmentPermissions().show("相机,录音,文件读写", childFragmentManager)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null || requestCode != REQUEST_CODE_CHOOSE) {
            cancleChooseFile()
            return
        }
        handleResult(Matisse.obtainPathResult(data))
    }


    /**
     * 处理结果回调
     */
    private fun handleResult(paths: List<String>) {
        createFlowable<Array<Uri>> {
            val files = Luban.with(requireContext())
                .load(paths)
                .ignoreBy(100)
                .setTargetDir(CacheGlobal.getLubanCacheDir())
                .filter {
                    !(TextUtils.isEmpty(it) || it.toLowerCase(Locale.getDefault()).endsWith(".gif"))
                }.get()
            onNext(files.map {
                it.toUri()
            }.toTypedArray())
            onComplete()
        }.bindToSchedulers().autoLoading(viewModel).autoDispose(viewModel)
            .subscribe({
                if (uploadFile != null) {
                    uploadFile?.onReceiveValue(it[0].toCast())
                } else if (uploadMultipleFile != null) {
                    uploadMultipleFile?.onReceiveValue(it.toCast())
                }
            },{
                it.message.toString().toast()
            })
    }

    /**
     * 取消选择
     */
    private fun cancleChooseFile() {
        if (uploadFile != null) {
            uploadFile?.onReceiveValue(null)
        } else if (uploadMultipleFile != null) {
            uploadMultipleFile?.onReceiveValue(null)
        }
    }
}