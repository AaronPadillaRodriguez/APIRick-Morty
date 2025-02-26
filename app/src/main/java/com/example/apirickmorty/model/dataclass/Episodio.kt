package com.example.apirickmorty.model.dataclass

import com.google.gson.annotations.SerializedName

/**
 * Clase de datos que representa un episodio de la serie Rick y Morty.
 *
 * @property id Identificador único del episodio.
 * @property name Nombre del episodio.
 * @property air_date Fecha de emisión del episodio.
 * @property episode Código del episodio (formato: S00E00).
 * @property characters Lista de URLs de los personajes que aparecen en el episodio.
 * @property url URL del recurso API para este episodio específico.
 * @property created Fecha y hora de creación del registro en la base de datos.
 */
data class Episodio(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("air_date") val air_date: String,
    @SerializedName("episode") val episode: String,
    @SerializedName("characters") val characters: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String
)