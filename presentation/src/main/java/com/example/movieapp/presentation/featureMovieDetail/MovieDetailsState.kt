package com.example.movieapp.presentation.featureMovieDetail

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presentation.util.UiText

data class MovieDetailsState(
    val movieDetails: Movie? = null,
    val isFavorite: Boolean = false,
    val loading: Boolean = true,
    val error: UiText? = null,
    val isSheetOpen: Boolean = false,
)
