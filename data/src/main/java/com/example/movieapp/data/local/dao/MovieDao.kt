package com.example.movieapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.movieapp.data.local.entity.FullMovieEntity
import com.example.movieapp.data.local.entity.MovieEntity
import com.example.movieapp.data.local.entity.MovieListEntity
import com.example.movieapp.data.local.entity.MovieListWithMovie
import com.example.movieapp.data.local.entity.MovieMovieListCrossRef
import com.example.movieapp.domain.model.MovieCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Upsert
    suspend fun upsertMovieList(list: List<MovieEntity>)

    @Upsert
    suspend fun upsertMovieItem(item: MovieEntity)

    @Upsert
    suspend fun upsertFullMovieItem(item: FullMovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(list: MovieListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieListCrossRef(crossRef: List<MovieMovieListCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieCrossRef(crossRef: MovieMovieListCrossRef)

    @Query("SELECT COUNT(*) FROM movielistentity WHERE category = :category")
    suspend fun isMovieListCached(category: MovieCategory): Boolean


    @Query("SELECT COUNT(*) FROM moviemovielistcrossref WHERE category = :category AND kinopoiskId = :id")
    fun isMovieInList(category: MovieCategory, id: Int): Flow<Boolean>

    @Query("DELETE FROM moviemovielistcrossref WHERE category = :category")
    suspend fun deleteMovieListCrossRef(category: MovieCategory)


    @Query("DELETE FROM moviemovielistcrossref WHERE category = :category AND kinopoiskId = :id")
    suspend fun deleteMovieCrossRef(category: MovieCategory, id: Int)

//    @Query("DELETE FROM movieentity WHERE kinopoiskid IN(:ids)")
//    suspend fun deleteMovie(ids: List<Int>)

    @Query("SELECT * FROM movieentity LIMIT 30")
    fun pagingMovie(): List<MovieEntity>

    @Query("DELETE FROM movieentity")
    suspend fun clearAll()

    @Query("SELECT * FROM movieentity WHERE kinopoiskid = :ids")
    suspend fun getMovie(ids: Int): MovieEntity?

    @Transaction
    @Query("SELECT * FROM fullmovieentity WHERE kinopoiskid = :ids")
    fun getFullMovie(ids: Int): Flow<FullMovieEntity?>

    @Query("SELECT * FROM movielistentity")
    suspend fun getListMovie(): List<MovieListEntity>?

    @Query("SELECT * FROM movielistentity WHERE category = :category")
    fun getMovieList(category: MovieCategory): Flow<List<MovieListWithMovie>>
}