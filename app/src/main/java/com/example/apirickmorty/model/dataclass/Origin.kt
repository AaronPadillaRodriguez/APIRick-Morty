package com.example.apirickmorty.model.dataclass

import com.google.gson.annotations.SerializedName

data class Origin(
    @SerializedName("name") val name: String
)