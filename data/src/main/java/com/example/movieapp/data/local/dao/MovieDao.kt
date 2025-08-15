package com.example.movieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movieapp.data.local.entity.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(list: List<MovieEntity>)

    @Query("DELETE FROM movieentity WHERE kinopoiskid IN(:ids)")
    suspend fun deleteMovie(ids: List<Int>)

    @Query("SELECT * FROM movieentity WHERE kinopoiskid IN(:ids)")
    suspend fun getMovie(ids: Int): List<MovieEntity>
}