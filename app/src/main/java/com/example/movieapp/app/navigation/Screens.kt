package com.example.movieapp.app.navigation

import com.example.movieapp.domain.model.MovieCategory
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens {
    @Serializable
    data object HomeScreen : Screens

    @Serializable
    data class MovieListScreen(
        val category: MovieCategory
    ) : Screens

    @Serializable
    data class MovieDetailsScreen(
        val movieId: Int,
    ) : Screens

    @Serializable
    data object SearchScreen : Screens

    @Serializable
    data object FavScreen : Screens
}