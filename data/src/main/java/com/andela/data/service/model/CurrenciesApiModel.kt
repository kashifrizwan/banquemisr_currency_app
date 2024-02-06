package com.andela.data.service.model

import com.google.gson.annotations.SerializedName

data class CurrenciesApiModel(
    @SerializedName("success")
    val success: Boolean = false,

    @SerializedName("symbols")
    val symbols: HashMap<String, String> = HashMap(),

    @SerializedName("error")
    val error: Error
)

data class Error (
    @SerializedName("code")
    val code: Int = 0,

    @SerializedName("type")
    val type: String = "Undefined",

    @SerializedName("info")
    val info: String = "Unknown Error"
)
