package com.example.movieapp.data.api

import androidx.room.Index
import com.example.movieapp.data.api.models.FilmCollectionResponseDTO
import com.example.movieapp.data.api.models.MovieDTO
import com.example.movieapp.data.api.models.SeasonResponseDTO
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.model.Order
import com.example.movieapp.domain.model.Type
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {
    @GET("/api/v2.2/films/collections")
    suspend fun getCollection(
        @Query("type") type: MovieCategory? = null,
        @Query("page") page: Int = 1,
    ): Response<FilmCollectionResponseDTO>

    @GET("/api/v2.2/films/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int,
    ): MovieDTO

    @GET("/api/v2.2/films")
    suspend fun searchMovie(
        @Query("keyword") keyword: String? = "",

        @Query("ratingFrom") ratingFrom: Float? = null,
        @Query("ratingTo") ratingTo: Float? = null,

        @Query("yearFrom") yearFrom: Int? = null,
        @Query("yearTo") yearTo: Int? = null,

        @Query("order") order: Order? = null,

        @Query("type") type: Type? = null,

        @Query("page") page: Int = 1,
    ): FilmCollectionResponseDTO

    @GET("/api/v2.2/films/{movieId}/seasons")
    suspend fun getSeasonOverview(
        @Path("movieId") movieId: Int,
    ): SeasonResponseDTO
}