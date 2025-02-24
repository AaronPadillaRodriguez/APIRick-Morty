package com.example.apirickmorty.model.dataclass

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("name") val name: String
)