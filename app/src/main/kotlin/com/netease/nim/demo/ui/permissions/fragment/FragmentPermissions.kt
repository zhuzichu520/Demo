package com.netease.nim.demo.ui.permissions.fragment

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.FragmentManager
import com.hiwitech.android.libs.tool.setOnClickListener
import com.hiwitech.android.mvvm.base.ArgDefault
import com.netease.nim.demo.BR
import com.netease.nim.demo.R
import com.netease.nim.demo.base.DialogFragmentBase
import com.netease.nim.demo.databinding.DialogFragmentPermissionsBinding
import com.netease.nim.demo.ui.permissions.viewmodel.ViewModelPermissions
import kotlinx.android.synthetic.main.dialog_fragment_permissions.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/7 5:55 PM
 * since: v 1.0.0
 */
class FragmentPermissions :
    DialogFragmentBase<DialogFragmentPermissionsBinding, ViewModelPermissions, ArgDefault>(),
    View.OnClickListener {

    private lateinit var content: String

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.dialog_fragment_permissions

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initView() {
        super.initView()
        setOnClickListener(this, left, right)
        viewModel.content.value = String.format("该功能需要您的%s权限，请前往设置中心进行授权", content)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.left -> {
                dismiss()
            }
            R.id.right -> {
                jumpPermissionPage(requireContext())
                dismiss()
            }
        }
    }

    fun show(content: String, fragmentManager: FragmentManager) {
        this.content = content
        show(fragmentManager, "FragmentPermissions")
    }

    /**
     * 跳转到权限设置界面
     *
     * @param context
     */
    private fun jumpPermissionPage(context: Context) {
        try {
            when (Build.MANUFACTURER) {
                "HUAWEI" -> goHuaWeiMainager(context)
                "vivo" -> goVivoMainager(context)
                "OPPO" -> goOppoMainager(context)
                "Coolpad" -> goCoolpadMainager(context)
                "Meizu" -> goMeizuMainager(context)
                "Xiaomi" -> goXiaoMiMainager(context)
                "samsung" -> goSangXinMainager(context)
                "Sony" -> goSonyMainager(context)
                "LG" -> goLGMainager(context)
                else -> goIntentSetting(context)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun goLGMainager(context: Context) {
        try {
            val intent = Intent(context.packageName)
            val comp = ComponentName(
                "com.android.settings",
                "com.android.settings.Settings\$AccessLockSummaryActivity"
            )
            intent.component = comp
            context.startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            goIntentSetting(context)
        }
    }

    private fun goSonyMainager(context: Context) {
        try {
            val intent = Intent(context.packageName)
            val comp = ComponentName(
                "com.sonymobile.cta",
                "com.sonymobile.cta.SomcCTAMainActivity"
            )
            intent.component = comp
            context.startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            goIntentSetting(context)
        }
    }

    private fun goHuaWeiMainager(context: Context) {
        try {
            val intent = Intent(context.packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity"
            )
            intent.component = comp
            context.startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            goIntentSetting(context)
        }
    }

    private fun getMiuiVersion(): String? {
        val propName = "ro.miui.ui.version.name"
        val line: String
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $propName")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        } finally {
            try {
                input!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return line
    }

    private fun goXiaoMiMainager(context: Context) {
        val rom = getMiuiVersion()
        val intent = Intent()
        if ("V6" == rom || "V7" == rom) {
            intent.action = "miui.intent.action.APP_PERM_EDITOR"
            intent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
            )
            intent.putExtra("extra_pkgname", context.packageName)
        } else if ("V8" == rom || "V9" == rom) {
            intent.action = "miui.intent.action.APP_PERM_EDITOR"
            intent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity"
            )
            intent.putExtra("extra_pkgname", context.packageName)
        } else {
            goIntentSetting(context)
        }
        context.startActivity(intent)
    }

    private fun goMeizuMainager(context: Context) {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", context.packageName)
            context.startActivity(intent)
        } catch (localActivityNotFoundException: ActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace()
            goIntentSetting(context)
        }
    }

    private fun goSangXinMainager(mContext: Context) { //三星4.3可以直接跳转
        goIntentSetting(mContext)
    }

    private fun goIntentSetting(mContext: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri =
            Uri.fromParts("package", mContext.packageName, null)
        intent.data = uri
        try {
            mContext.startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun goOppoMainager(mContext: Context) {
        doStartApplicationWithPackageName(
            "com.coloros.safecenter",
            mContext
        )
    }

    private fun goCoolpadMainager(mContext: Context) {
        doStartApplicationWithPackageName(
            "com.yulong.android.security:remote",
            mContext
        )
    }

    private fun goVivoMainager(mContext: Context) {
        doStartApplicationWithPackageName(
            "com.bairenkeji.icaller",
            mContext
        )
    }

    private fun doStartApplicationWithPackageName(packagename: String, context: Context) {
        var packageinfo: PackageInfo? = null
        try {
            packageinfo = context.packageManager.getPackageInfo(packagename, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageinfo == null) {
            return
        }
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.setPackage(packageinfo.packageName)
        val resolveinfoList = context.packageManager.queryIntentActivities(resolveIntent, 0)
        val resolveinfo = resolveinfoList.iterator().next()
        if (resolveinfo != null) {
            val packageName = resolveinfo.activityInfo.packageName
            val className = resolveinfo.activityInfo.name
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val cn = ComponentName(packageName, className)
            intent.component = cn
            try {
                context.startActivity(intent)
            } catch (e: java.lang.Exception) {
                goIntentSetting(context)
                e.printStackTrace()
            }
        }
    }

}