package com.example.movieapp.presentation.featureHome

import com.example.movieapp.domain.model.MovieCategory

sealed interface HomeAction {
    data class ItemClickAction(val movieId: Int): HomeAction
    data class OnCategoryClick(val movieCategory: MovieCategory): HomeAction

    data object Refresh: HomeAction
}