package com.example.movieapp.presentation.featureMovieDetail

sealed interface MovieDetailsAction {
    data object Update : MovieDetailsAction

    data object NavigateBack : MovieDetailsAction

    data class SeasonOverview(val movieId: Int) : MovieDetailsAction

    data object SheetUpdate : MovieDetailsAction
    data class AddToFav(val movieId: Int) : MovieDetailsAction
}
