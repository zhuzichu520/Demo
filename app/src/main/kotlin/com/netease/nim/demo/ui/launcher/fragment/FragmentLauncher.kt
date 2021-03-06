package com.netease.nim.demo.ui.launcher.fragment

import android.Manifest
import com.hiwitech.android.libs.internal.MainHandler
import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.FragmentBase
import com.netease.nim.demo.databinding.FragmentMainBinding
import com.netease.nim.demo.storage.NimUserStorage
import com.netease.nim.demo.ui.launcher.viewmodel.ViewModelLauncher
import com.netease.nim.demo.ui.login.ActivityLogin
import com.netease.nim.demo.ui.login.main.arg.ArgLogin
import com.netease.nim.demo.ui.main.ActivityMain
import com.netease.nim.demo.ui.permissions.fragment.FragmentPermissions
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.uber.autodispose.autoDispose

/**
 * desc 启动Fragment
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class FragmentLauncher : FragmentBase<FragmentMainBinding, ViewModelLauncher, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.fragment_launcher

    override fun initView() {
        super.initView()
        RxPermissions(this).request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).autoDispose(viewModel).subscribe {
            if (it) {
                initX5WebView()
                MainHandler.postDelayed {
                    if (!NimUserStorage.isLogin()) {
                        startActivity(
                            ActivityLogin::class.java,
                            isPop = true,
                            arg = ArgLogin(false)
                        )
                    } else {
                        startActivity(ActivityMain::class.java, isPop = true)
                    }
                }
            } else {
                FragmentPermissions().show("文件读写", parentFragmentManager)
            }
        }

    }

    private fun initX5WebView() {
        QbSdk.initX5Environment(
            requireContext().applicationContext,
            object : QbSdk.PreInitCallback {

                override fun onCoreInitFinished() {
                    val map = mutableMapOf<String, Any>()
                    map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
                    QbSdk.initTbsSettings(map)
                }

                override fun onViewInitFinished(isInit: Boolean) {

                }
            })
    }
}