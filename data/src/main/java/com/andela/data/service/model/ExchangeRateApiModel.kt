package com.andela.data.service.model

import com.google.gson.annotations.SerializedName

data class ExchangeRateApiModel(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("timestamp")
    val timestamp: Long,

    @SerializedName("base")
    val base: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("rates")
    val rates: HashMap<String, Double> = HashMap(),

    @SerializedName("error")
    val error: Error
)
