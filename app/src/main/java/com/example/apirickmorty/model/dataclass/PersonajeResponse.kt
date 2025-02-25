package com.example.apirickmorty.model.dataclass

import com.google.gson.annotations.SerializedName

/**
 * Clase de respuesta para la API que encapsula la información sobre personajes.
 *
 * @property info Objeto con información sobre la paginación.
 * @property results Lista de objetos Personaje obtenidos de la API.
 */
data class PersonajeResponse(
    @SerializedName("info") val info: Info,
    @SerializedName("results") val results: List<Personaje>
)
