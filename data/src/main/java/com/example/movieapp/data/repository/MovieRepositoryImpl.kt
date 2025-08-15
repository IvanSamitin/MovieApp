package com.example.movieapp.data.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapp.data.api.MoviePagingSource
import com.example.movieapp.data.api.KinopoiskApi
import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.mappers.toMovie
import com.example.movieapp.data.mappers.toMovieEntity
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
    private val dao: MovieDao
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


    override suspend fun getMovieDetails(movieId: Int): Result<Movie, DataError.Network> =
        withContext(Dispatchers.IO) {

            val localMovie = dao.getMovie(movieId)?.toMovie()
            if (localMovie != null){
                return@withContext Result.Success(localMovie)
            }

            try {
                val dto = api.getMovieDetail(movieId)
                dao.upsertMovieItem(dto.toMovieEntity())
                Result.Success(dto.toMovie())

            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            }
        }
}
