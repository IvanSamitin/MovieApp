package com.example.movieapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieapp.domain.model.MovieCategory

@Entity
data class MovieListEntity(
    @PrimaryKey(autoGenerate = false) val category: MovieCategory
)
