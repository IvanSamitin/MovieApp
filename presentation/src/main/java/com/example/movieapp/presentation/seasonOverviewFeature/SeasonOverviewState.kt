package com.example.movieapp.presentation.seasonOverviewFeature

import com.example.movieapp.domain.model.Season
import com.example.movieapp.presentation.util.UiText

data class SeasonOverviewState(
    val loading: Boolean = false,
    val error: UiText? = null,
    val season: List<Season> = emptyList(),
)