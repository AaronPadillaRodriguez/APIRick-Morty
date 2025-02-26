package com.example.apirickmorty.model.dataclass

import com.google.gson.annotations.SerializedName

/**
 * Clase de datos que contiene información de paginación para los resultados de la API.
 *
 * @property count Número total de recursos disponibles en la API.
 * @property pages Número total de páginas disponibles para consultar.
 */
data class Info(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int
)
