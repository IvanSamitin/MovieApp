package com.example.movieapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieapp.data.local.convertors.Converters
import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.local.entity.FullMovieEntity
import com.example.movieapp.data.local.entity.MovieEntity
import com.example.movieapp.data.local.entity.MovieListEntity
import com.example.movieapp.data.local.entity.MovieMovieListCrossRef

@Database(
    entities = [
        MovieEntity::class,
        MovieListEntity::class,
        MovieMovieListCrossRef::class,
        FullMovieEntity::class
               ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MovieDatabase: RoomDatabase() {
    abstract val dao: MovieDao
}