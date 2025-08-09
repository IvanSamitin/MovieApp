package com.example.movieapp.presentation.featureMovieList

import com.example.movieapp.data.api.models.MovieDTO
import com.example.movieapp.domain.model.Movie

data class MovieListState(
    val movieDetails: List<Movie>? = null,
    val loading: Boolean = true,
    val error: String? = null,
    val isRefreshing: Boolean = false
)