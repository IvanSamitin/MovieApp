package com.example.movieapp.presentation.featureMovieList

sealed interface MovieListAction {
    data object ActionUpdate : MovieListAction
    data class ItemClickAction(val id: Int) : MovieListAction
    data object PullToRefreshAction: MovieListAction
}