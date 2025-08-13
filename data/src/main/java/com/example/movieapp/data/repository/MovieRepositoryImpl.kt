package com.example.movieapp.data.repository


import com.example.movieapp.data.api.KinopoiskApi
import com.example.movieapp.data.mappers.toMovie
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.resultLogic.DataError
import com.example.movieapp.domain.resultLogic.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

class MovieRepositoryImpl(
    private val api: KinopoiskApi,
) : MovieRepository {
    override suspend fun getCollection(
        type: MovieCategory,
        page: Int,
    ): Result<List<Movie>, DataError.Network> =
        withContext(Dispatchers.IO) {
            try {
                val dto = api.getCollection(type, page)
                Result.Success(dto.items.map { it.toMovie() })
            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            }
        }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie, DataError.Network> =
        withContext(Dispatchers.IO) {
            try {
                val dto = api.getMovieDetail(movieId)
                Result.Success(dto.toMovie())
            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            }
        }
}
