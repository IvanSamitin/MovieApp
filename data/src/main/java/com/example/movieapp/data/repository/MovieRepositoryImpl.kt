package com.example.movieapp.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapp.data.api.MoviePagingSource
import com.example.movieapp.data.api.KinopoiskApi
import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.local.entity.MovieListEntity
import com.example.movieapp.data.local.entity.MovieMovieListCrossRef
import com.example.movieapp.data.mappers.toFullMovieEntity
import com.example.movieapp.data.mappers.toMovie
import com.example.movieapp.data.mappers.toMovieEntity
import com.example.movieapp.data.mappers.toSeason
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.domain.model.MovieDetails
import com.example.movieapp.domain.model.Order
import com.example.movieapp.domain.model.Season
import com.example.movieapp.domain.model.Type
import com.example.movieapp.domain.repository.MovieRepository
import com.example.movieapp.domain.resultLogic.DataError
import com.example.movieapp.domain.resultLogic.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.http.Query

class MovieRepositoryImpl(
    private val api: KinopoiskApi,
    private val dao: MovieDao
) : MovieRepository {

    override suspend fun getCollection(
        type: MovieCategory,
        page: Int,
    ): Flow<Result<List<Movie>, DataError.Network>> = flow {
        val isCached = try {
            dao.isMovieListCached(type)
        } catch (e: Exception) {
            false
        }

        if (isCached) {
            val cached = dao.getMovieList(type)
                .map { movieListWithMovies ->
                    val items = movieListWithMovies
                        .flatMap { it.listMovie.map { movieItem -> movieItem.toMovie() } }
                    Result.Success<List<Movie>, DataError.Network>(items)
                }
            emitAll(cached)
        }

        try {
            val response = api.getCollection(type, page)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!

                dao.upsertMovieList(body.items.map { it.toMovieEntity() })
                dao.deleteMovieListCrossRef(category = type)
                dao.insertMovieList(MovieListEntity(category = type))
                val crossRef = body.items.map {
                    MovieMovieListCrossRef(
                        kinopoiskId = it.kinopoiskId!!,
                        category = type
                    )
                }
                dao.insertMovieListCrossRef(crossRef)

                val updated = dao.getMovieList(type)
                    .map { movieListWithMovies ->
                        val items = movieListWithMovies
                            .flatMap { it.listMovie.map { movieItem -> movieItem.toMovie() } }
                        Result.Success<List<Movie>, DataError.Network>(items)
                    }
                emitAll(updated)
            } else {
                emit(Result.Error(DataError.Network.SERVER_ERROR))
            }
        } catch (e: IOException) {
            emit(Result.Error(DataError.Network.NO_INTERNET))
        }
    }.flowOn(Dispatchers.IO)

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

    override suspend fun syncListMovie() {
        val saveCategory = dao.getListMovie()?.map { it.category } ?: return
        val filteredCategory = saveCategory.filter { it != MovieCategory.FAVORITE }

        Log.d("DEBUG", filteredCategory.toString())

        for (category in filteredCategory) {
            val dto = api.getCollection(category, 1)
            if (dto.isSuccessful && dto.body() != null) {
                dao.upsertMovieList(dto.body()!!.items.map { it.toMovieEntity() })
                dao.deleteMovieListCrossRef(category = category)
                dao.insertMovieList(MovieListEntity(category = category))
                val crossRef = dto.body()!!.items.map {
                    MovieMovieListCrossRef(
                        kinopoiskId = it.kinopoiskId!!,
                        category = category
                    )
                }
                dao.insertMovieListCrossRef(crossRef)
            }
        }
    }

    override suspend fun addToList(movieId: Int, category: MovieCategory) {
        withContext(Dispatchers.IO) {
            val isHaveList = dao.isMovieListCached(category = category)
            if (!isHaveList) dao.insertMovieList(MovieListEntity(category = category))
            val crossRef = MovieMovieListCrossRef(
                kinopoiskId = movieId,
                category = category
            )
            dao.insertMovieCrossRef(crossRef)
        }
    }

    override suspend fun deleteFromList(movieId: Int, category: MovieCategory) {
        withContext(Dispatchers.IO) {
            dao.deleteMovieCrossRef(category = category, id = movieId)
        }
    }

    private fun isMovieInList(movieId: Int, category: MovieCategory): Flow<Boolean> =
        dao.isMovieInList(category = category, id = movieId)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMovieDetails(movieId: Int): Flow<Result<MovieDetails, DataError.Network>> =
        flow {
            val localMovie = dao.getFullMovie(movieId).firstOrNull()
            val isActual = localMovie?.let {
                System.currentTimeMillis() - it.lastUpdated < TTL
            } ?: false

            val movieFlow = if (localMovie != null && isActual) {
                flowOf(localMovie.toMovieEntity().toMovie())
            } else {
                flow {
                    val dto = api.getMovieDetail(movieId)
                    dao.upsertFullMovieItem(dto.toFullMovieEntity())
                    emit(dto.toMovie())
                }
            }

            emitAll(
                movieFlow.flatMapConcat { movie ->
                    isMovieInList(movieId, MovieCategory.FAVORITE)
                        .map { isInFavorites ->
                            Result.Success<MovieDetails, DataError.Network>(
                                MovieDetails(
                                    movie = movie,
                                    isInFavorites = isInFavorites
                                )
                            ) as Result<MovieDetails, DataError.Network>
                        }
                }
            )
        }
            .catch { e ->
                emit(Result.Error(handleNetworkError(e)))
            }
            .flowOn(Dispatchers.IO)

    private fun handleNetworkError(e: Throwable): DataError.Network =
        when (e) {
            is IOException -> DataError.Network.NO_INTERNET
            else -> DataError.Network.SERVER_ERROR
        }

    override suspend fun searchMovie(
        keyword: String,
        ratingFrom: Float,
        ratingTo: Float,
        order: Order?,
        type: Type?,
    ): Result<List<Movie>, DataError.Network> =
        withContext(Dispatchers.IO) {
            try {
                val dto = api.searchMovie(
                    keyword = keyword, ratingFrom = ratingFrom,
                    ratingTo = ratingTo,
                    order = order,
                    type = type,
                )
                dao.upsertMovieList(dto.items.map { it.toMovieEntity() })
                Result.Success(dto.items.map { it.toMovie() })

            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            }
        }

    override suspend fun getSeasonOverview(movieId: Int): Result<List<Season>, DataError.Network> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getSeasonOverview(movieId = movieId)
                Result.Success(response.items.map { it.toSeason() })
            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            }
        }

    override suspend fun getRandomListMovie(): Result<List<Movie>, DataError.Network> =
        withContext(Dispatchers.IO) {
            try {
                val listMovie = dao.pagingMovie().shuffled()
                Result.Success(listMovie.map { it.toMovie() })
            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            }
        }

    companion object {
        const val TTL: Long = 24 * 60 * 60 * 1000
    }
}