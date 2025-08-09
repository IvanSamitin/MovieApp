package com.example.movieapp.data.api

import com.example.movieapp.data.api.models.FilmCollectionResponseDTO
import com.example.movieapp.data.api.models.MovieDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {

    @GET("/api/v2.2/films/collections")
    suspend fun getCollection(
        @Query("type") type: CollectionType? = null,
        @Query("page") page: Int = 1
    ): FilmCollectionResponseDTO

    @GET("/api/v2.2/films/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int
    ): MovieDTO
}

@Serializable
enum class CollectionType {
    TOP_POPULAR_ALL,

    @SerialName("TOP_250_MOVIES")
    TOP_250_MOVIES
}