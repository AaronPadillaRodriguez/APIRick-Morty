package com.example.apirickmorty.model

import com.example.apirickmorty.model.dataclass.Episodio
import com.example.apirickmorty.model.dataclass.EpisodioResponse
import com.example.apirickmorty.model.dataclass.Personaje
import com.example.apirickmorty.model.dataclass.PersonajeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("character")
    suspend fun getCharactersByPage(@Query("page") page: Int): PersonajeResponse

    @GET("character/{id}")
    suspend fun getPersonaje(@Path("id") id: Int): Personaje

    @GET("episode/{id}")
    suspend fun getEpisodio(@Path("id") id: Int): Episodio

    @GET("episode")
    suspend fun getEpisodiosByTemporada(@Query("episode") episode: String): EpisodioResponse

    @GET("episode/?episode=E01")
    suspend fun getTemporadas(): EpisodioResponse
}