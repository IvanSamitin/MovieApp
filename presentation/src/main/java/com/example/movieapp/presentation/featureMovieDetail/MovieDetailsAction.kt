package com.example.movieapp.presentation.featureMovieDetail

sealed interface MovieDetailsAction {
    data object Update : MovieDetailsAction

    data class AddToFav(val movieId: Int) : MovieDetailsAction
}
