package com.example.apirickmorty.model.dataclass

import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int
)
