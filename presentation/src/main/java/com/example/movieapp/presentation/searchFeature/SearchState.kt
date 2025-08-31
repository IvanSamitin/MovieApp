package com.example.movieapp.presentation.searchFeature

import com.example.movieapp.domain.model.Movie

data class SearchState(
    val searchText: String = "",
    val listMovie: List<Movie> = emptyList(),
    val loading: Boolean = false,
    val error: String = "",
    val isInputTextError: Boolean = false,
    val searchErrorText: String = "",
    val isDialogOpen: Boolean = false,
    val sliderState: ClosedFloatingPointRange<Float> = 0f..10f,
    val typeMenuValue: String = "",
    val orderMenuValue: String = "",
)

