package com.example.movieapp.data.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapp.data.MoviePagingSource
import com.example.movieapp.data.api.KinopoiskApi
import com.example.movieapp.data.mappers.toMovie
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.resultLogic.DataError
import com.example.movieapp.domain.resultLogic.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
                if (dto.isSuccessful) {
                    Result.Success(dto.body()!!.items.map { it.toMovie() })
                } else {
                    Result.Error(DataError.Network.SERVER_ERROR)
                }
            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            }
        }

    override fun getPagingCollection(movieCategory: MovieCategory): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                initialLoadSize = 20
            ),
            pagingSourceFactory = {
                MoviePagingSource(api = api, movieCategory = movieCategory)
            }
        ).flow
    }

    private var movieCache = mutableMapOf<Int, Movie>()

    override suspend fun getMovieDetails(movieId: Int): Result<Movie, DataError.Network> =
        withContext(Dispatchers.IO) {
            movieCache[movieId]?.let {
                return@withContext Result.Success(it)
            }
            try {
                val dto = api.getMovieDetail(movieId)
                movieCache[movieId] = dto.toMovie()
                Result.Success(dto.toMovie())

            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            }
        }
}
