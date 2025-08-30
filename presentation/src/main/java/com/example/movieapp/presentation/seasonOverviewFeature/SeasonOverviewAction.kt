package com.example.movieapp.presentation.seasonOverviewFeature

sealed interface SeasonOverviewAction {
    data object NavigateBack : SeasonOverviewAction

    data object Retry : SeasonOverviewAction
}