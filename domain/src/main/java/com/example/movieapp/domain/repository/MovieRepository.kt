package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.resultLogic.DataError
import com.example.movieapp.domain.resultLogic.Result

interface MovieRepository {
    suspend fun getCollection(
        type: MovieCategory,
        page: Int,
    ): Result<List<Movie>, DataError.Network>

    suspend fun getMovieDetails(movieId: Int): Result<Movie, DataError.Network>
}
