package com.example.movieapp.presentation.seasonOverviewFeature

import com.example.movieapp.domain.model.Season

data class SeasonOverviewState(
    val loading: Boolean = false,
    val error: String = "",
    val season: List<Season> = emptyList(),
)