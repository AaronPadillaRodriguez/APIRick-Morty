package com.example.apirickmorty.model.dataclass

import com.google.gson.annotations.SerializedName

/**
 * Clase de datos que representa la ubicación actual de un personaje.
 *
 * @property name Nombre de la ubicación donde se encuentra el personaje.
 */
data class Location(
    @SerializedName("name") val name: String
)