package com.netease.nim.demo.nim.config

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.netease.nimlib.push.net.lbs.IPVersion
import com.netease.nimlib.push.packet.asymmetric.AsymmetricType
import com.netease.nimlib.push.packet.symmetry.SymmetryType
import com.netease.nimlib.sdk.NimHandshakeType
import com.netease.nimlib.sdk.ServerAddresses
import org.json.JSONException
import org.json.JSONObject

object NimPrivatizationConfig {

    /// BASIC
    private const val KEY_APP_KEY = "appkey"

    private const val KEY_MODULE = "module"

    private const val KEY_VERSION = "version"

    private const val KEY_HAND_SHAKE_TYPE = "hand_shake_type"

    /// MAIN LINK
    private const val KEY_LBS = "lbs"

    private const val KEY_LINK = "link"

    /// NOS UPLOAD
    private const val KEY_HTTPS_ENABLED = "https_enabled"

    private const val KEY_NOS_LBS = "nos_lbs"

    private const val KEY_NOS_UPLOADER = "nos_uploader"

    private const val KEY_NOS_UPLOADER_HOST = "nos_uploader_host"

    /// NOS DOWNLOAD
    private const val KEY_NOS_DOWNLOADER = "nos_downloader"

    private const val KEY_NOS_ACCELERATE = "nos_accelerate"

    private const val KEY_NOS_ACCELERATE_HOST = "nos_accelerate_host"

    /// SERVER
    private const val KEY_NT_SERVER = "nt_server"

    // 握手协议(国密)
    private const val KEY_DEDICATED_CLUSTE_FLAG = "dedicated_cluste_flag"
    private const val KEY_NEGO_KEY_NECA = "nego_key_neca"
    private const val KEY_NEGO_KEY_ENCA_KEY_VERSION = "nego_key_enca_key_version"
    private const val KEY_NEGO_KEY_ENCA_KEY_PARTA = "nego_key_enca_key_parta"
    private const val KEY_NEGO_KEY_ENCA_KEY_PARTB = "nego_key_enca_key_partb"
    private const val KEY_COMM_ENCA = "comm_enca"

    // IM IPv6
    private const val KEY_LINK_IPV6 = "link_ipv6"
    private const val KEY_IP_PROTOCOL_VERSION = "ip_protocol_version"
    private const val KEY_PROBE_IPV4_URL = "probe_ipv4_url"
    private const val KEY_PROBE_IPV6_URL = "probe_ipv6_url"


    private const val SHARE_NAME = "nim_demo_private_config"

    private const val KEY_CONFIG_ENABLE = "private_config_enable"

    private const val KEY_CONFIG_JSON = "private_config_json"


    private const val KEY_CHAT_ROOM_LIST_URL = "chatroomDemoListUrl"


    private const val BUCKET_NAME_PLACE_HOLDER = "{bucket}"

    private const val OBJECT_PLACE_HOLDER = "{object}"


    private const val CONFIG_URL = "config_private_url"

    private var appKey: String? = null

    fun getAppKey(context: Context): String? {
        if (isPrivateDisable(context)) {
            return null
        }
        if (appKey != null) {
            return appKey
        }
        val jsonObject = getConfig(context) ?: return null
        try {
            appKey = jsonObject.getString(KEY_APP_KEY)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return appKey
    }


    fun getServerAddresses(context: Context): ServerAddresses? {
        return if (isPrivateDisable(context)) {
            null
        } else parseAddresses(getConfig(context))
    }


    fun updateConfig(config: String?, context: Context) {
        if (TextUtils.isEmpty(config)) {
            return
        }
        getSP(context).edit().putString(KEY_CONFIG_JSON, config).apply()
    }


    fun getChatListRoomUrl(context: Context): String? {
        return getConfig(context)!!.optString(KEY_CHAT_ROOM_LIST_URL)
    }

    fun getConfigUrl(context: Context): String? {
        return getSP(context).getString(CONFIG_URL, null)
    }

    fun saveConfigUrl(context: Context, url: String?) {
        getSP(context).edit().putString(CONFIG_URL, url).apply()
    }

    fun enablePrivateConfig(
        enable: Boolean,
        context: Context
    ) {
        getSP(context).edit().putBoolean(KEY_CONFIG_ENABLE, enable).apply()
    }

    fun isPrivateDisable(context: Context): Boolean {
        return !getSP(context).getBoolean(KEY_CONFIG_ENABLE, false)
    }

    fun getConfig(context: Context): JSONObject? {
        val configStr = getSP(context).getString(KEY_CONFIG_JSON, null)
        return if (TextUtils.isEmpty(configStr)) {
            null
        } else parse(configStr)
    }

    fun checkConfig(config: String?): ServerAddresses? {
        return parseAddresses(parse(config))
    }

    private fun parseAddresses(jsonObject: JSONObject?): ServerAddresses? {
        if (jsonObject == null) {
            return null
        }
        val addresses = ServerAddresses()
        addresses.handshakeType = NimHandshakeType.value(
            jsonObject.optInt(
                KEY_HAND_SHAKE_TYPE,
                NimHandshakeType.V1.value
            )
        )
        addresses.module = jsonObject.optString(KEY_MODULE)
        addresses.publicKeyVersion = jsonObject.optInt(KEY_VERSION, 0)
        addresses.lbs = jsonObject.optString(KEY_LBS)
        addresses.defaultLink = jsonObject.optString(KEY_LINK)
        addresses.nosUploadLbs = jsonObject.optString(KEY_NOS_LBS)
        addresses.nosUploadDefaultLink = jsonObject.optString(KEY_NOS_UPLOADER)
        addresses.nosUpload = jsonObject.optString(KEY_NOS_UPLOADER_HOST)
        addresses.nosSupportHttps = jsonObject.optBoolean(KEY_HTTPS_ENABLED, false)
        addresses.nosDownloadUrlFormat = jsonObject.optString(KEY_NOS_DOWNLOADER)
        addresses.nosDownload = jsonObject.optString(KEY_NOS_ACCELERATE_HOST)
        addresses.nosAccess = jsonObject.optString(KEY_NOS_ACCELERATE)
        addresses.ntServerAddress = jsonObject.optString(KEY_NT_SERVER)
        addresses.dedicatedClusteFlag = jsonObject.optInt(KEY_DEDICATED_CLUSTE_FLAG)
        addresses.negoKeyNeca = AsymmetricType.value(
            jsonObject.optInt(
                KEY_NEGO_KEY_NECA,
                AsymmetricType.RSA.value
            )
        )
        addresses.negoKeyEncaKeyVersion = jsonObject.optInt(KEY_NEGO_KEY_ENCA_KEY_VERSION)
        addresses.negoKeyEncaKeyParta = jsonObject.optString(KEY_NEGO_KEY_ENCA_KEY_PARTA)
        addresses.negoKeyEncaKeyPartb = jsonObject.optString(KEY_NEGO_KEY_ENCA_KEY_PARTB)
        addresses.commEnca =
            SymmetryType.value(jsonObject.optInt(KEY_COMM_ENCA, SymmetryType.RC4.value))
        addresses.linkIpv6 = jsonObject.optString(KEY_LINK_IPV6)
        addresses.ipProtocolVersion =
            IPVersion.value(jsonObject.optInt(KEY_IP_PROTOCOL_VERSION, IPVersion.IPV4.value))
        addresses.probeIpv4Url = jsonObject.optString(KEY_PROBE_IPV4_URL)
        addresses.probeIpv6Url = jsonObject.optString(KEY_PROBE_IPV6_URL)
        appKey = jsonObject.optString(KEY_APP_KEY)
        autoAdjust(addresses)
        checkValid(addresses)
        return addresses
    }

    private fun checkValid(addresses: ServerAddresses) {
        require(!TextUtils.isEmpty(addresses.lbs)) { "ServerAddresses lbs is null" }
        require(!TextUtils.isEmpty(addresses.nosUploadLbs)) { "ServerAddresses nosUploadLbs is null" }
        require(!TextUtils.isEmpty(addresses.defaultLink)) { "ServerAddresses  defaultLink is null" }
        require(!TextUtils.isEmpty(addresses.nosUploadDefaultLink)) { "ServerAddresses nosUploadDefaultLink is null" }
        require(!TextUtils.isEmpty(addresses.nosDownloadUrlFormat)) { "ServerAddresses nosDownloadUrlFormat is null" }
        require(checkFormatValid(addresses.nosDownloadUrlFormat)) { "ServerAddresses nosDownloadUrlFormat is illegal" }
        require(!(addresses.nosSupportHttps && TextUtils.isEmpty(addresses.nosUpload))) { "ServerAddresses nosSupportHttps is true , but  nosUpload is null" }
    }

    /**
     * 自动调整字段，避免去改其他地方的逻辑
     */
    private fun autoAdjust(addresses: ServerAddresses) {
        addresses.module =
            if (TextUtils.isEmpty(addresses.module)) null else addresses.module
        addresses.lbs = if (TextUtils.isEmpty(addresses.lbs)) null else addresses.lbs
        addresses.defaultLink =
            if (TextUtils.isEmpty(addresses.defaultLink)) null else addresses.defaultLink
        addresses.nosUploadLbs =
            if (TextUtils.isEmpty(addresses.nosUploadLbs)) null else addresses.nosUploadLbs
        addresses.nosUploadDefaultLink =
            if (TextUtils.isEmpty(addresses.nosUploadDefaultLink)) null else addresses.nosUploadDefaultLink
        addresses.nosUpload =
            if (TextUtils.isEmpty(addresses.nosUpload)) null else addresses.nosUpload
        addresses.nosDownloadUrlFormat =
            if (TextUtils.isEmpty(addresses.nosDownloadUrlFormat)) null else addresses.nosDownloadUrlFormat
        addresses.nosDownload =
            if (TextUtils.isEmpty(addresses.nosDownload)) null else addresses.nosDownload
        addresses.nosAccess =
            if (TextUtils.isEmpty(addresses.nosAccess)) null else addresses.nosAccess
        addresses.ntServerAddress =
            if (TextUtils.isEmpty(addresses.ntServerAddress)) null else addresses.ntServerAddress
        addresses.negoKeyEncaKeyParta =
            if (TextUtils.isEmpty(addresses.negoKeyEncaKeyParta)) null else addresses.negoKeyEncaKeyParta
        addresses.negoKeyEncaKeyPartb =
            if (TextUtils.isEmpty(addresses.negoKeyEncaKeyPartb)) null else addresses.negoKeyEncaKeyPartb
        addresses.linkIpv6 =
            if (TextUtils.isEmpty(addresses.linkIpv6)) null else addresses.linkIpv6
        addresses.probeIpv4Url =
            if (TextUtils.isEmpty(addresses.probeIpv4Url)) null else addresses.probeIpv4Url
        addresses.probeIpv6Url =
            if (TextUtils.isEmpty(addresses.probeIpv6Url)) null else addresses.probeIpv6Url
        appKey = if (TextUtils.isEmpty(appKey)) null else appKey
    }

    private fun getSP(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE)
    }

    private fun parse(json: String?): JSONObject? {
        return try {
            JSONObject(json.toString())
        } catch (e: JSONException) {
            null
        }
    }


    private fun checkFormatValid(format: String): Boolean {
        return !TextUtils.isEmpty(format) && format.contains(BUCKET_NAME_PLACE_HOLDER) && format.contains(
            OBJECT_PLACE_HOLDER
        )
    }

}