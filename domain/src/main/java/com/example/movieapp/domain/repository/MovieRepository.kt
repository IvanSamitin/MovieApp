package com.example.movieapp.domain.repository


import androidx.paging.PagingData
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.resultLogic.DataError
import com.example.movieapp.domain.resultLogic.Result
import kotlinx.coroutines.flow.Flow


interface MovieRepository {
    suspend fun getCollection(
        type: MovieCategory,
        page: Int,
    ): Result<List<Movie>, DataError.Network>
    fun getPagingCollection(movieCategory: MovieCategory): Flow<PagingData<Movie>>

    suspend fun getMovieDetails(movieId: Int): Result<Movie, DataError.Network>
}
