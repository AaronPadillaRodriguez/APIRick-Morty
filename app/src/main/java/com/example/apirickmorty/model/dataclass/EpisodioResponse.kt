package com.example.apirickmorty.model.dataclass

import com.google.gson.annotations.SerializedName

data class EpisodioResponse(
    @SerializedName("info") val info: Info,
    @SerializedName("results") val results: List<Episodio>
)