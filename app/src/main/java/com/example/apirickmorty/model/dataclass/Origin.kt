package com.example.apirickmorty.model.dataclass

import com.google.gson.annotations.SerializedName

/**
 * Clase de datos que representa el origen o ubicación de nacimiento de un personaje.
 *
 * @property name Nombre del lugar de origen del personaje.
 */
data class Origin(
    @SerializedName("name") val name: String
)