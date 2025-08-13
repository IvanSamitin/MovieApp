package com.example.movieapp.data.api

import com.example.movieapp.data.api.models.FilmCollectionResponseDTO
import com.example.movieapp.data.api.models.MovieDTO
import com.example.movieapp.domain.model.MovieCategory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {
    @GET("/api/v2.2/films/collections")
    suspend fun getCollection(
        @Query("type") type: MovieCategory? = null,
        @Query("page") page: Int = 1,
    ): FilmCollectionResponseDTO

    @GET("/api/v2.2/films/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int,
    ): MovieDTO
}