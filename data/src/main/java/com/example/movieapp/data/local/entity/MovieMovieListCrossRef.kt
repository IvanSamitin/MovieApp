package com.example.movieapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.movieapp.domain.model.MovieCategory

@Entity(
    primaryKeys = ["kinopoiskId", "category"],
    foreignKeys = [
        ForeignKey(
            entity = MovieListEntity::class,
            parentColumns = ["category"],
            childColumns = ["category"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MovieEntity::class,
            parentColumns = ["kinopoiskId"],
            childColumns = ["kinopoiskId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MovieMovieListCrossRef(
    val kinopoiskId: Int,
    val category: MovieCategory
)
