package com.example.apirickmorty.model.dataclass

import com.google.gson.annotations.SerializedName

/**
 * Clase de respuesta para la API que encapsula la información sobre episodios.
 *
 * @property info Objeto con información sobre la paginación.
 * @property results Lista de objetos Episodio obtenidos de la API.
 */
data class EpisodioResponse(
    @SerializedName("info") val info: Info,
    @SerializedName("results") val results: List<Episodio>
)