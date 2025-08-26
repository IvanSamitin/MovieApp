package com.example.movieapp.presentation.featureMovieDetail

import com.example.movieapp.domain.model.Movie

data class MovieDetailsState(
    val movieDetails: Movie? = null,
    val isFavorite: Boolean = false,
    val loading: Boolean = true,
    val error: String? = null,
)
