package com.hiwitech.android.shared.http.entity

import com.google.gson.annotations.SerializedName

data class NimReponse<T>(
    @SerializedName("data")
    var `data`: T? = null,
    @SerializedName("res")
    var res: Int? = null,
    @SerializedName("errmsg")
    var errmsg: String? = null
)