package com.example.movieapp.presentation.featureHome

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory

data class HomeState(
    val movieCollection: Map<MovieCategory, List<Movie>> = emptyMap(),
    val loading: Boolean = true,
    val error: String? = null,
)