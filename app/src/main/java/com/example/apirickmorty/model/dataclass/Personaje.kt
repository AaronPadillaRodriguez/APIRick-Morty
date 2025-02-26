package com.example.apirickmorty.model.dataclass

import com.google.gson.annotations.SerializedName

/**
 * Clase de datos que representa un personaje de la serie Rick y Morty.
 *
 * @property id Identificador único del personaje.
 * @property name Nombre del personaje.
 * @property status Estado actual del personaje (vivo, muerto, desconocido).
 * @property species Especie a la que pertenece el personaje.
 * @property type Tipo o subcategoría específica del personaje (si aplica).
 * @property gender Género del personaje.
 * @property origen Lugar de origen del personaje.
 * @property location Ubicación actual del personaje.
 * @property image URL de la imagen del personaje.
 * @property episode Lista de URLs de episodios en los que aparece el personaje.
 * @property url URL del recurso API para este personaje específico.
 * @property created Fecha y hora de creación del registro en la base de datos.
 */
data class Personaje(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("type") val type: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("origin") val origen: Origin,
    @SerializedName("location") val location: Location,
    @SerializedName("image") val image: String,
    @SerializedName("episode") val episode: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String
)