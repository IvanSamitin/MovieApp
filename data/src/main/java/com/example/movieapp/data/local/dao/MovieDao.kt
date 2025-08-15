package com.example.movieapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.movieapp.data.local.entity.MovieEntity

@Dao
interface MovieDao {

    @Upsert
    suspend fun upsertMovieList(list: List<MovieEntity>)

    @Upsert
    suspend fun upsertMovieItem(item: MovieEntity)

//    @Query("DELETE FROM movieentity WHERE kinopoiskid IN(:ids)")
//    suspend fun deleteMovie(ids: List<Int>)

    @Query("SELECT * FROM movieentity")
    fun pagingMovie(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movieentity")
    suspend fun clearAll()

    @Query("SELECT * FROM movieentity WHERE kinopoiskid = :ids")
    suspend fun getMovie(ids: Int): MovieEntity?


}