package com.andela.data.service.model

import com.google.gson.annotations.SerializedName

data class GenericApiErrorModel(
    @SerializedName("message")
    val message: String = "Undefined Error!"
)
