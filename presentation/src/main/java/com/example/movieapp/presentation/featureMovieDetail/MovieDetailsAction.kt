package com.example.movieapp.presentation.featureMovieDetail

sealed interface MovieDetailsAction {
    data object Update : MovieDetailsAction
}
