package com.example.movieapp.presentation.featureHome

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieCategory
import com.example.movieapp.presentation.util.UiText

data class HomeState(
    val movieCollection: Map<MovieCategory, List<Movie>> = emptyMap(),
    val loading: Boolean = true,
    val error: UiText? = null,
    val isConnected: Boolean = true
)