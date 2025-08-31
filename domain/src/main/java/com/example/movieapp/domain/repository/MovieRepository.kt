package com.example.movieapp.domain.repository


import androidx.paging.PagingData
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.model.MovieDetails
import com.example.movieapp.domain.model.Order
import com.example.movieapp.domain.model.Season
import com.example.movieapp.domain.model.Type
import com.example.movieapp.domain.resultLogic.DataError
import com.example.movieapp.domain.resultLogic.Result
import kotlinx.coroutines.flow.Flow


interface MovieRepository {
    suspend fun getCollection(
        type: MovieCategory,
        page: Int,
    ): Flow<Result<List<Movie>, DataError.Network>>

    fun getPagingCollection(movieCategory: MovieCategory): Flow<PagingData<Movie>>

    fun getMovieDetails(movieId: Int): Flow<Result<MovieDetails, DataError.Network>>

    suspend fun searchMovie(
        keyword: String, ratingFrom: Float,
        ratingTo: Float,
        order: Order?,
        type: Type?,
    ): Result<List<Movie>, DataError.Network>

    suspend fun syncListMovie()

    suspend fun deleteFromList(movieId: Int, category: MovieCategory)

    suspend fun addToList(movieId: Int, category: MovieCategory)

    suspend fun getSeasonOverview(movieId: Int): Result<List<Season>, DataError.Network>
}
