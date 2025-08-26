package com.example.movieapp.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieListWithMovie(
    @Embedded val movieListEntity: MovieListEntity,
    @Relation(
        parentColumn = "category",
        entityColumn = "kinopoiskId",
        associateBy = Junction(MovieMovieListCrossRef::class)
    )
    val listMovie: List<MovieEntity>
)