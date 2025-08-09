package com.example.movieapp.domain.repository

import com.example.movieapp.data.api.CollectionType

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.result_logic.DataError
import com.example.movieapp.domain.result_logic.Result

interface MovieRepository {
    suspend fun getCollection(type: CollectionType, page: Int): Result<List<Movie>, DataError.Network>

    suspend fun getMovieDetails(movieId: Int): Result<Movie, DataError.Network>
}