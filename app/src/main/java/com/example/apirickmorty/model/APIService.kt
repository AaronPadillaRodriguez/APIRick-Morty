package com.example.apirickmorty.model

import com.example.apirickmorty.model.dataclass.Episodio
import com.example.apirickmorty.model.dataclass.EpisodioResponse
import com.example.apirickmorty.model.dataclass.Personaje
import com.example.apirickmorty.model.dataclass.PersonajeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz para definir los endpoints de la API de Rick y Morty.
 */
interface APIService {

    /**
     * Obtiene una página específica de personajes.
     *
     * @param page Número de página a obtener.
     * @return Objeto PersonajeResponse con los resultados paginados.
     */
    @GET("character")
    suspend fun getCharactersByPage(@Query("page") page: Int): PersonajeResponse

    /**
     * Obtiene un personaje específico por su ID.
     *
     * @param id ID del personaje a obtener.
     * @return Objeto Personaje con la información del personaje.
     */
    @GET("character/{id}")
    suspend fun getPersonaje(@Path("id") id: Int): Personaje

    /**
     * Obtiene un episodio específico por su ID.
     *
     * @param id ID del episodio a obtener.
     * @return Objeto Episodio con la información del episodio.
     */
    @GET("episode/{id}")
    suspend fun getEpisodio(@Path("id") id: Int): Episodio

    /**
     * Obtiene episodios filtrados por temporada.
     *
     * @param episode Código de temporada (formato "S0x").
     * @return Objeto EpisodioResponse con los episodios de la temporada especificada.
     */
    @GET("episode")
    suspend fun getEpisodiosByTemporada(@Query("episode") episode: String): EpisodioResponse

    /**
     * Obtiene todos los primeros episodios de cada temporada.
     *
     * @return Objeto EpisodioResponse con los primeros episodios de cada temporada.
     */
    @GET("episode/?episode=E01")
    suspend fun getTemporadas(): EpisodioResponse
}