package com.netease.nim.demo.nim.config

import android.content.Context
import android.text.TextUtils
import com.netease.nim.demo.ActivityMain
import com.netease.nim.demo.nim.provider.NimUserInfoProvider
import com.netease.nimlib.sdk.SDKOptions
import com.netease.nimlib.sdk.ServerAddresses
import com.netease.nimlib.sdk.StatusBarNotificationConfig
import com.netease.nimlib.sdk.mixpush.MixPushConfig
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.zhuzichu.android.libs.tool.getScreenWidth
import com.zhuzichu.android.libs.tool.toCast
import com.zhuzichu.android.shared.global.CacheGlobal

object NimSDKOptionConfig {
    fun getSDKOptions(context: Context): SDKOptions {
        val options = SDKOptions()
        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        initStatusBarNotificationConfig(options)
        // 配置 APP 保存图片/语音/文件/log等数据的目录
        // 配置 APP 保存图片/语音/文件/log等数据的目录
        options.sdkStorageRootPath = CacheGlobal.getNimCacheDir()
        // 配置是否需要预下载附件缩略图
        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true
        // 配置附件缩略图的尺寸大小
        // 配置附件缩略图的尺寸大小
        options.thumbnailSize = (165.0 / 320.0 * getScreenWidth(context)).toCast()
        // 通知栏显示用户昵称和头像
        // 通知栏显示用户昵称和头像
        options.userInfoProvider =
            NimUserInfoProvider(context)
        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = messageNotifierCustomization
        // 在线多端同步未读数
        // 在线多端同步未读数
        options.sessionReadAck = true
        // 动图的缩略图直接下载原图
        // 动图的缩略图直接下载原图
        options.animatedImageThumbnailEnabled = true
        // 采用异步加载SDK
        // 采用异步加载SDK
        options.asyncInitSDK = true
        // 是否是弱IM场景
        // 是否是弱IM场景
        options.reducedIM = false
        // 是否检查manifest 配置，调试阶段打开，调试通过之后请关掉
        // 是否检查manifest 配置，调试阶段打开，调试通过之后请关掉
        options.checkManifestConfig = false
        // 是否启用群消息已读功能，默认关闭
        // 是否启用群消息已读功能，默认关闭
        options.enableTeamMsgAck = true
        // 打开消息撤回未读数-1的开关
        // 打开消息撤回未读数-1的开关
        options.shouldConsiderRevokedMessageUnreadCount = true
        // 云信私有化配置项
        // 云信私有化配置项
        configServerAddress(options, context)
        options.mixPushConfig = buildMixPushConfig()
        //        options.mNosTokenSceneConfig = createNosTokenScene();
        //        options.mNosTokenSceneConfig = createNosTokenScene();
        options.loginCustomTag = "登录自定义字段"
        return options
    }

    private val messageNotifierCustomization: MessageNotifierCustomization = object :
        MessageNotifierCustomization {
        override fun makeNotifyContent(nick: String, message: IMMessage): String? {
            return null // 采用SDK默认文案
        }

        override fun makeTicker(nick: String, message: IMMessage): String? {
            return null // 采用SDK默认文案
        }

        override fun makeRevokeMsgTip(revokeAccount: String, item: IMMessage): String {
            return revokeAccount.plus("撤回了一条消息")
        }
    }

    private fun configServerAddress(
        options: SDKOptions,
        context: Context
    ) {
        val serverConfig: ServerAddresses? = NimPrivatizationConfig.getServerAddresses(context)
        options.serverConfig = serverConfig
        val appKey: String? = NimPrivatizationConfig.getAppKey(context)
        if (!TextUtils.isEmpty(appKey)) {
            options.appKey = appKey
        }
    }

    private fun initStatusBarNotificationConfig(options: SDKOptions) {
        val config = loadStatusBarNotificationConfig()
    }

    private fun loadStatusBarNotificationConfig(): StatusBarNotificationConfig {
        val config = StatusBarNotificationConfig()
        config.notificationEntrance = ActivityMain::class.java
        return config
    }

    private fun buildMixPushConfig(): MixPushConfig { // 第三方推送配置
        val config = MixPushConfig()
        // 小米推送
        config.xmAppId = "2882303761517502883"
        config.xmAppKey = "5671750254883"
        config.xmCertificateName = "DEMO_MI_PUSH"
        // 华为推送
        config.hwAppId = "101420927"
        config.hwCertificateName = "DEMO_HW_PUSH"
        // 魅族推送
        config.mzAppId = "111710"
        config.mzAppKey = "282bdd3a37ec4f898f47c5bbbf9d2369"
        config.mzCertificateName = "DEMO_MZ_PUSH"
        // fcm 推送，适用于海外用户，不使用fcm请不要配置
        config.fcmCertificateName = "DEMO_FCM_PUSH"
        // vivo推送
        config.vivoCertificateName = "DEMO_VIVO_PUSH"
        // oppo推送
        config.oppoAppId = "3477155"
        config.oppoAppKey = "6clw0ue1oZ8cCOogKg488o0os"
        config.oppoAppSercet = "e163705Bd018bABb3e2362C440A94673"
        config.oppoCertificateName = "DEMO_OPPO_PUSH"
        return config
    }
}